module.exports = function(grunt) {

  grunt.initConfig({

    uglify: {
      build: {
        src: ['public/javascripts/app/*.js'],
        dest: 'public/javascripts/lib/main.js'
      }
    },

    watch: {
      options: {
        livereload: true
      },
      app: {
        files: ['public/*.*', 'app/views/*.html']
      },
      scripts: {
        files: ['public/javascripts/app/*.js'],
        tasks: ['uglify']
      }
    },

  });

  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-uglify');


  grunt.registerTask('default', ['watch']);

};