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

    copy: {
      all: {
        files: [
          {expand: true, src: ['client/**'], dest: '/Volumes/60GO (C)/play-2.2.1/Courses/public'}
        ]
      }
    }

  });

  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-copy');

  grunt.registerTask('default', ['watch']);

};