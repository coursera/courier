$('#right-nav').affix({
    offset: {
        top: $('#right-nav').offset().top
    }
});
anchors.options = {
  placement: 'left'
};
anchors.add('h1');
anchors.add('h2');
anchors.add('h3');
