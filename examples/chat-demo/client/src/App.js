import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import ChannelList from './ChannelList/ChannelList';

class App extends React.Component {
    render() {
        return (
            <Grid>
                <Row>
                    <Col sm={2}>
                        <ChannelList />
                    </Col>
                    <Col sm={8}>
                        <div></div>
                    </Col>
                    <Col sm={2}>
                        <div></div>
                    </Col>
                </Row>
            </Grid>
        );
    }
}

export default App;
