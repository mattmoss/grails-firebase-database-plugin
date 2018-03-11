import React from 'react';
import ChannelHeaderView from './ChannelHeaderView';

class ChannelHeader extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return this.props.channel ?
            <ChannelHeaderView channel={this.props.channel} /> :
            null;
    }
}

export default ChannelHeader;
