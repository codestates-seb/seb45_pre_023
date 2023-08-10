module.exports = {
    content: [
        "./src/**/*.{js,jsx,ts,tsx}",  
    ],
    theme: {
      extend: {
        spacing : {
          '42': '10.5rem',
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