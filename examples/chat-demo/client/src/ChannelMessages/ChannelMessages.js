import React from 'react';
import firebase from '../firebase';
import ChannelMessagesView from './ChannelMessagesView';

class ChannelMessages extends React.Component {

    state = {
        messages: [],
        users: { }
    };

    addMessage(message) {
        let messages = this.state.messages;
        this.setState({ messages: [...messages, message]});
    }

    getMessagesRef(channel) {
        return firebase.database().ref(`messages/${channel.key}`);
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
        const prevChannel = (this.props.channel || {}).key;
        const nextChannel = (nextProps.channel || {}).key;

        if (nextChannel !== prevChannel) {
            this.ignoreChannel(this.props.channel);
            this.clearMessages();
            this.listenChannel(nextProps.channel);
        }

        let users = { };
        nextProps.users.forEach(data => {
            users[data.key] = data;
        });
        this.setState({ users });
    }

    render() {
        return this.props.channel ?
            <ChannelMessagesView messages={this.state.messages}
                                 users={this.state.users} /> :
            null;
    }

}

export default ChannelMessages;
