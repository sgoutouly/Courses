module.exports = function(grunt) {

  grunt.initConfig({

    watch: {
      options: {
        livereload: true
      },
      app: {
        files: ['public/*.*']
      },
      scripts: {
        files: ['public/**/*.*', 'public/**/**/*.*', 'public/**/**/**/*.*']
      }
    }

  });

  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.registerTask('default', ['watch']);
  

};