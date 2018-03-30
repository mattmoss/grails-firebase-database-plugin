import React from 'react';
import { PageHeader } from 'react-bootstrap';

const ChannelHeader = ({ channel }) => (
    <PageHeader>
        <strong>
            {channel ? '#' + channel.name : '' }
        </strong> [ <i>
        {channel ? channel.topic : 'Select a channel from the list at left.'}
    </i> ]
    </PageHeader>
);

export default ChannelHeader;