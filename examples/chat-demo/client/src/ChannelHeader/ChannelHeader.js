import React from 'react';
import ChannelHeaderView from './ChannelHeaderView';

class ChannelHeader extends React.Component {
    render() {
        return <ChannelHeaderView channel={this.props.channel} />;
    }
}

export default ChannelHeader;
