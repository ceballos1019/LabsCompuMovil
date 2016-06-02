angular.module('starter.services', ['ngResource'])

//Servicio para buscar los detalles del clima de una ciudad, dado su nombre
.factory('searchService', function($resource) {
  return $resource("http://api.openweathermap.org/data/2.5/weather?q=:cityName&appid=1036902626c88e251c8ad4ab2cc839a5&units=metric&lang=es");
})

//Servicio para buscar los detalles del clima de una ciudad, dada su longitud y su latitud
.factory('Weather', function($http,$resource){
  return $resource("http://api.openweathermap.org/data/2.5/weather?lat=:latitude&lon=:longitude&appid=1036902626c88e251c8ad4ab2cc839a5&units=metric&lang=es"); 
});
