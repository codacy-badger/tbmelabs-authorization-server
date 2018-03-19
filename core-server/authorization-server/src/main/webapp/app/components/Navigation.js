// @flow
'use strict';

import React, {Component} from 'react';
import PropTypes from 'prop-types';

import {LinkContainer} from 'react-router-bootstrap';

import Navbar from 'react-bootstrap/lib/Navbar';
import Nav from 'react-bootstrap/lib/Nav';
import NavItem from 'react-bootstrap/lib/NavItem';
import NavDropdown from 'react-bootstrap/lib/NavDropdown';
import MenuItem from 'react-bootstrap/lib/MenuItem';

require('bootstrap/dist/css/bootstrap.css');

const LOGOUT_EVENT = 'LOGOUT';

class Navigation extends Component<Navigation.propTypes> {
  onClick: () => void;

  constructor(props: Navigation.propTypes) {
    super(props);

    this.onClick = this.onClick.bind(this);
  }

  onClick(event: SyntheticInputEvent<HTMLInputElement>) {
    const {logout} = this.props;

    switch (event.target.name) {
      case LOGOUT_EVENT:
        logout();
        break;
    }
  }

  render() {
    const {texts} = this.props;

    return (
      <Navbar collapseOnSelect>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="#">TBME Labs</a>
          </Navbar.Brand>
          <Navbar.Toggle/>
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav>
            {/*<NavItem eventKey={1} href="#">Link</NavItem>*/}
            {/*<NavItem eventKey={2} href="#">Link</NavItem>*/}
            {/*<NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">*/}
            {/*<MenuItem eventKey={3.1}>Action</MenuItem>*/}
            {/*<MenuItem eventKey={3.2}>Another action</MenuItem>*/}
            {/*<MenuItem eventKey={3.3}>Something else here</MenuItem>*/}
            {/*<MenuItem divider/>*/}
            {/*<MenuItem eventKey={3.3}>Separated link</MenuItem>*/}
            {/*</NavDropdown>*/}
          </Nav>
          <Nav pullRight>
            <NavDropdown title={texts.account} id="account-dropdown">
              <LinkContainer to='profile'>
                <MenuItem>{texts.account}</MenuItem>
              </LinkContainer>
              <MenuItem onClick={this.props.logout}>{texts.logout}</MenuItem>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}

Navigation.propTypes = {
  logout: PropTypes.func.isRequired,
  texts: PropTypes.object.isRequired
}

export default Navigation;