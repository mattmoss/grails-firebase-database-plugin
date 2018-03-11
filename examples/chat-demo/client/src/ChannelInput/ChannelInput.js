import React from 'react';
import ChannelInputView from './ChannelInputView';

class ChannelInput extends React.Component {

    constructor(props) {
        super(props);
        this.state = { message: '' };
    }

    componentDidMount() {

    }

    render() {
        const editMessage = message => this.setState({ message });

        const sendMessage = () => { console.log('Send message:' + this.state.message); };

        return this.props.channel ?
            <ChannelInputView message={this.state.message}
                              channel={this.props.channel}
                              onChange={editMessage}
                              onSubmit={sendMessage} /> :
            null;
    }

}

export default ChannelInput;