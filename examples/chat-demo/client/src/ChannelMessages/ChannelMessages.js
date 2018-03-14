import React from 'react';
import firebase from '../firebase';
import ChannelMessagesView from './ChannelMessagesView';

class ChannelMessages extends React.Component {

    state = {
        messages: []
    };

    addMessage(message) {
        let messages = this.state.messages;
        this.setState({ messages: [...messages, message]});
    }

    getMessagesRef(channel) {
        return firebase.database().ref(`outgoing/${channel.key}`);
    }

    listenChannel(channel) {
        if (channel) {
            this.getMessagesRef(channel).on('child_added', snapshot => {
                this.addMessage({ key: snapshot.key, ...snapshot.val() });
            });
        }
    }

    ignoreChannel(channel) {
        if (channel) {
            this.getMessagesRef(channel).off();
        }
    }

    clearMessages() {
        this.setState({ messages: [] });
    }

    componentWillReceiveProps(nextProps) {
        this.ignoreChannel(this.props.channel);
        this.clearMessages();
        this.listenChannel(nextProps.channel);
    }

    render() {
        return this.props.channel ?
            <ChannelMessagesView messages={this.state.messages} /> :
            null;
    }

}

export default ChannelMessages;
