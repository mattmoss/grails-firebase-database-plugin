import React from 'react';
import { Grid, Row, Col, Button, PageHeader } from 'react-bootstrap';
import firebase from 'firebase';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import ChannelList from './ChannelList/ChannelList';
import ChannelHeader from './ChannelHeader/ChannelHeader';
import ChannelInput from './ChannelInput/ChannelInput';
import ChannelMessages from './ChannelMessages/ChannelMessages';
import UsersList from './UsersList/UsersList';

const uiConfig = {
    signInFlow: 'popup',
    signInOptions: [
        firebase.auth.EmailAuthProvider.PROVIDER_ID,
        firebase.auth.GoogleAuthProvider.PROVIDER_ID,
        // firebase.auth.FacebookAuthProvider.PROVIDER_ID
    ],
    callbacks: {
        // Avoid redirects after sign-in.
        signInSuccess: () => false
    }
};

class App extends React.Component {

    state = {
        channels: [],
        active: null,
        users: [],
        user: null,
    };

    loadChannels() {
        const channelsRef = firebase.database().ref('channels').orderByChild('name');

        channelsRef.on('value', snapshot => {
            let channels = [];
            snapshot.forEach(data => {
                channels.push({ key: data.key, ...data.val() });
            });
            this.setState({ channels });
        });
    }

    loadUsers() {
        const usersRef = firebase.database().ref('users').orderByChild('displayName');

        usersRef.on('value', snapshot => {
            let users = [];
            snapshot.forEach(data => {
                const val = data.val();
                users.push({ key: data.key, displayName: val.displayName });
            });
            this.setState({ users });
        });
    }

    watchAuthentication() {
        firebase.auth().onAuthStateChanged(user => {
            this.setState({ active: null, user });
            if (!!user) {
                firebase.database().ref(`users/${user.uid}`).set(
                    { displayName: user.displayName }
                ).then(
                    () => null,
                    error => console.error('Failed to send user info:', error)
                );
            }
        });
    }

    componentDidMount() {
        this.loadChannels();
        this.loadUsers();
        this.watchAuthentication();
    }

    render() {
        if (!this.state.user) {
            return (
                <Grid fluid>
                    <Row>
                        <Col>
                            <h1 className="text-center">Grails/Firebase Chat Demo</h1>
                            <StyledFirebaseAuth uiConfig={uiConfig} firebaseAuth={firebase.auth()} />
                        </Col>
                    </Row>
                </Grid>
            );
        }
        else {
            const selectChannel = channel => this.setState({active: channel});

            return (
                <Grid fluid>
                    <Row>
                        <Col sm={2}>
                            <PageHeader><strong>Channels</strong></PageHeader>
                            <ChannelList channels={this.state.channels}
                                         active={this.state.active}
                                         onSelect={selectChannel} />
                        </Col>
                        <Col sm={8}>
                            <div>
                                <ChannelHeader channel={this.state.active} />
                                <ChannelMessages channel={this.state.active} />
                                <ChannelInput channel={this.state.active} />
                            </div>
                        </Col>
                        <Col sm={2}>
                            <PageHeader>
                                <strong>{this.state.user.displayName}</strong>
                            </PageHeader>
                            <div>
                                <Button className="btn-sm" bsStyle="danger"
                                        onClick={() => firebase.auth().signOut()}>
                                    Sign-Out
                                </Button>
                            </div>
                            <PageHeader>
                                <strong>Members</strong>
                            </PageHeader>
                            <UsersList users={this.state.users} />
                        </Col>
                    </Row>
                </Grid>
            );
        }
    }

}

export default App;
