// @flow

import type Language from './types/language.type';
import {SET_LANGUAGE} from '../actions/language';

const en = require('../config/languages/en.json');

const initialState: Language = {
  name: en.language,
  texts: en,
  id: 'en'
}

export default (state: Language = initialState, action: { payload: string } = {}): Language => {
  switch (action.type) {
    case SET_LANGUAGE:
      try {
        const language = require(`../config/languages/${action.payload}.json`);

        return {
          name: language.name,
          texts: language,
          id: action.payload
        }
      } catch (error) {
        return state;
      }
    default:
      return state;
  }
}