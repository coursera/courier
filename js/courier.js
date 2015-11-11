$('#right-nav').affix({
    offset: {
        top: $('#right-nav').offset().top
    }
});
anchors.options = {
  placement: 'left'
};
anchors.add('h2');
