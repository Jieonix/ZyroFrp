export default {
  plugins: {
    'postcss-pxtorem': {
      rootValue: 16,
      propList: ['*'],
      unitPrecision: 5,
      minPixelValue: 1,
    }
  }
}