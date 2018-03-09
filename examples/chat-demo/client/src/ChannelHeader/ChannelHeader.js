import React from 'react';
import ChannelHeaderView from './ChannelHeaderView';

class ChannelHeader extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return <ChannelHeaderView channel={this.props.channel} />;
    }
}

export default ChannelHeader;
