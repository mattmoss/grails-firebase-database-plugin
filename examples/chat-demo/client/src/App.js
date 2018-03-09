import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import ChannelList from './ChannelList/ChannelList';
import ChannelHeader from "./ChannelHeader/ChannelHeader";

class App extends React.Component {

    constructor() {
        super();
        this.state = { active: {} };
    }

    render() {
        const selectChannel = channel => this.setState({ active: channel });

        return (
            <Grid>
                <Row>
                    <Col sm={2}>
                        <ChannelList active={this.state.active}
                                     onSelectChannel={selectChannel} />
                    </Col>
                    <Col sm={8}>
                        <div>
                            <ChannelHeader channel={this.state.active} />
                        </div>
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
