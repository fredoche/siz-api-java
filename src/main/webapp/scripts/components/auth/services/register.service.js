'use strict';

angular.module('apiApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


