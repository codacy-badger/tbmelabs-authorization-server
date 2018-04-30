// @flow

import type FlashMessage from './types/flashMessage.type';
import {ADD_FLASH_MESSAGE, REMOVE_FLASH_MESSAGE} from '../actions/flashmessage';

import shortid from 'shortid';
import findIndex from 'lodash/findIndex';

const initialState: FlashMessage[] = [];

export default (state: FlashMessage[] = initialState, action = {payload: FlashMessage | number}): FlashMessage => {
  switch (action.type) {
    case ADD_FLASH_MESSAGE:
      return [
        ...state,
        {
          id: shortid.generate(),
          type: action.payload.type,
          title: action.payload.title,
          text: action.payload.text
        }
      ];
    case REMOVE_FLASH_MESSAGE:
      const index = findIndex(state, {id: action.payload});

      if (index >= 0) {
        return [
          ...state.slice(0, index),
          ...state.slice(index + 1)
        ];
      }

      return [...state];
    default:
      return state;
  }
}