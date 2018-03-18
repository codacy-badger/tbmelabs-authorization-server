'use strict';

import {SET_PROFILE} from '../actions/types';

const initialState = {}

export default (state = initialState, action = {}) => {
  switch (action.type) {
    case SET_PROFILE:
      return {
        state
      }
    default:
      return state;
  }
}