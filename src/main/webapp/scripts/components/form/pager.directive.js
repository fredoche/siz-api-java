/* globals $ */
'use strict';

angular.module('apiApp')
    .directive('apiAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
