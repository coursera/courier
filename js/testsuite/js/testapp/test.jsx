/* @flow */

import React from 'react';
import FortunePropType from '../propTypes/Fortune';

class Test extends React.Component {
  render() {
    return (
      <div>
        <WithFortune
          fortune={{
            'telling': 'Today is your lucky day',
            'createdAt': '2015-01-01T00:00:000Z'
          }} />
      </div>
    );
  }
}

class WithFortune extends React.Component {

}
WithFortune.propTypes = {
  fortune: FortunePropType
  /*fortune: React.PropTypes.shape({
    telling: React.PropTypes.string,
    createdAt: React.PropTypes.string
  })*/
};



module.exports = Test;
