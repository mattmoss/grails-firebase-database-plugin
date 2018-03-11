import React from 'react';
import firebase from '../firebase';
import ChannelInputView from './ChannelInputView';

class ChannelInput extends React.Component {

    constructor(props) {
        super(props);
        this.state = { message: '' };
    }

    componentDidMount() {

    }

    sendMessage() {
        firebase.database().ref(`incoming/${this.props.channel.name}`).push({
            author: 'foobar',
            message: this.state.message.trim(),
            timestamp: Date.now()
        }).then(
            value => {
                this.setState({ message: '' });
            },
            error => {
                console.error('Error while sending message:', error);
            }
        );
    }

    render() {
        const editMessage = message => this.setState({ message });

        return this.props.channel ?
            <ChannelInputView message={this.state.message}
                              channel={this.props.channel}
                              onChange={editMessage}
                              onSubmit={() => this.sendMessage()} /> :
            null;
    }

}

export default ChannelInput;