import React from 'react';
import { Table } from 'react-bootstrap';

const ChannelMessagesView = ({ messages, users }) => {

    const timestampFormat = new Intl.DateTimeFormat(undefined,
        { weekday: 'short', hour: '2-digit', minute: '2-digit', second: '2-digit' }
    );

    const formatTimestamp = timestamp => {
        return timestampFormat.format(new Date(timestamp));
    };

    return (
        <div id="chatMessages">
            <Table>
                <tbody>
                    {messages.map(message =>
                        <tr key={message.key}>
                            <td className="col-md-2 bg-info">{users[message.author].displayName}</td>
                            <td className="col-md-8">{message.message}</td>
                            <td className="col-md-2 text-right bg-success">{formatTimestamp(message.timestamp)}</td>
                        </tr>
                    )}
                </tbody>
            </Table>
        </div>
    );
};

export default ChannelMessagesView;
