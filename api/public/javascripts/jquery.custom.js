
jQuery(document).ready(function($) {






/* Nivo Slider
================================================== */

$(window).load(function(){

	$('#slider').nivoSlider();

});
	
	
	
	
	
	
/* Initialise SuperFish
================================================== */

jQuery(function(){
	jQuery('ul.nav').superfish();
});






/* Isotope, used for... quite a few things
================================================== */

$(window).load(function(){

// cache container
	var $container = $('#isotope');

	// initialize isotope
	$container.isotope({
		itemSelector : 'figure',
		layoutMode : 'fitRows',
		resizable: false, // disable normal resizing
		animationEngine : 'best-available'
	});

	// update columnWidth on window resize
	$(window).smartresize(function(){
	
		$container.isotope({
		
		});
	});

	// filter items when filter link is clicked
	$('#filters a').click(function(){
		var selector = $(this).attr('data-filter');
		$container.isotope({ filter: selector });
		return false;
	});

	// set selected menu items
	var $optionSets = $('.option-set'),
		$optionLinks = $optionSets.find('a');

		$optionLinks.click(function() {
			var $this = $(this);
			// don't proceed if already selected
			if ( $this.hasClass('selected') ) {
				return false;
			}

			var $optionSet = $this.parents('.option-set');
			$optionSet.find('.selected').removeClass('selected');
			$this.addClass('selected'); 
		});

});






/* Masonry (used for second portfolio version)
================================================== */

var $container = $('.masonry');

$container.imagesLoaded( function(){
	$container.masonry({
		itemSelector : 'figure',
		columnWidth: 240,
		isFitWidth: true,
		isAnimated: true
	});
});





/* Custom Tabs Script
================================================== */

$(document).ready(function() {

 //When page loads...
 $("ul.tabs li:first").addClass("active").show(); //Activate first tab
 $(".tab_content:first").show(); //Show first tab content

 //On Click Event
 $("ul.tabs li").click(function() {

  $("ul.tabs li").removeClass("active"); //Remove any "active" class
  $(this).addClass("active"); //Add "active" class to selected tab
  $(".tab_content").hide(); //Hide all tab content

  var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
  $(activeTab).fadeIn(); //Fade in the active ID content
  return false;
 });

});





/* Custom Accordian Script
================================================== */

$(document).ready(function() {

 //When page loads...
 $("ul.accordian li:first a.cord").addClass("active").show(); //Activate first accordian
 $("ul.accordian li:first .pane:first").show(); //Show first accordian content

 //On Click Event
 $("ul.accordian li a").click(function() {

  $("ul.accordian li a.cord").removeClass("active"); //Remove any "active" class
  $(this).addClass("active"); //Add "active" class to selected accordian
  $(".pane").slideUp(); //Hide all accordian content

  var activeTab = $(this).attr("href"); //Find the href attribute value to identify the active tab + content
  $(activeTab).slideDown(); //Fade in the active ID content
  return false;
 });

});





/* Hide the alerts on click
================================================== */

$("a.notify").click(function() {
	$(this).slideUp();
	return false;
});






/* The thumbnail/original replacer, with thanks to CodeBins.com
================================================== */

$('.preload').load(function(){
    if($(this).attr('src') != $(this).attr("data-original")){
	$(this).attr('src', $(this).attr("data-original"));
    }
});





/* End Custom JS
================================================== */

});