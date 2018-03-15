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
        active: null,
        user: null,
    };

    componentDidMount() {
        firebase.auth().onAuthStateChanged(user => {
            this.setState({ user });
            if (!!user) {
                console.log('User', user.displayName);
                console.log('UID', user.uid);
                firebase.database().ref(`users/${user.uid}`).set(
                    { displayName: user.displayName }
                ).then(
                    () => console.log('Successfully sent user info'),
                    error => console.error('Failed to send user info:', error)
                );
            }
        });
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
                            <ChannelList active={this.state.active}
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
                            <UsersList />
                        </Col>
                    </Row>
                </Grid>
            );
        }
    }

}

export default App;
