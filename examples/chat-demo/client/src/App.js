import React from 'react';
import { Grid, Row, Col, PageHeader } from 'react-bootstrap';
import firebase from 'firebase';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import ChannelList from './ChannelList/ChannelList';
import ChannelHeader from './ChannelHeader/ChannelHeader';
import ChannelInput from './ChannelInput/ChannelInput';
import ChannelMessages from './ChannelMessages/ChannelMessages';

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
        signedIn: false,
        active: null
    };

    componentDidMount() {
        firebase.auth().onAuthStateChanged(user =>
            this.setState({ signedIn: !!user })
        );
    }

    render() {
        if (!this.state.signedIn) {
            return (
                <Grid>
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
                <Grid>
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
                            <PageHeader><strong>Members</strong></PageHeader>
                            <div></div>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <a onClick={() => firebase.auth().signOut()}>Sign-Out</a>
                        </Col>
                    </Row>
                </Grid>
            );
        }
    }

}

export default App;
