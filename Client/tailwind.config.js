module.exports = {
    content: [
        "./src/**/*.{js,jsx,ts,tsx}",  
    ],
    theme: {
      extend: {
        spacing : {
          '42': '10.5rem',
          '56': '14rem',
          '64': '16rem',
          '80': '20rem',
          '96': '24rem',
          '128': '32rem',
          '132': '33rem',
          '144': '36rem',
          '152': '38rem',
          '160': '40rem',
          '168': '42rem',
          '176': '44rem',
          '180': '45rem',
          '184': '46rem',
        },
        borderWidth: {
          '1': '1px',
        },
        colors: {
          'stack': 'rgb(81 88 95);' 
        }
      },
    },
    plugins: [],
  }