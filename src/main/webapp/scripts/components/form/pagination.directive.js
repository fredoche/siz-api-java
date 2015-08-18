/* globals $ */
'use strict';

angular.module('apiApp')
    .directive('apiAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
