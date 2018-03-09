import React from 'react';
import firebase from '../firebase';
import ChannelListView from './ChannelListView';

class ChannelList extends React.Component {
    constructor() {
        super();
        this.state = { channels: [] };
    }

    componentDidMount() {
        const channelsRef = firebase.database().ref('channels').orderByChild('name');
        channelsRef.on('value', snapshot => {
            let channels = [];
            snapshot.forEach(data => {
                channels.push({ key: data.key, ...data.val() });
            });
            this.setState({ channels });
        });
    }

    render() {
        return <ChannelListView channels={this.state.channels} />;
    }
}

export default ChannelList;
