var config = {
  entry: './js/testapp/test.jsx',
  output: {
    filename: 'build/test.js'
  },
  resolve: {
    extensions: ['', '.js', '.jsx'],
    modulesDirectories: ['js', 'node_modules']
  },
  module: {
    loaders: [
      {
        test: /\.jsx?$/,
        exclude: /node_modules/,
        loaders: ['babel']
      },
    ]
  }
};

module.exports = config;
