'use strict';

angular.module('apiApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
