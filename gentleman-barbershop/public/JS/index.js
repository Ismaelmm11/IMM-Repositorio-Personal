document.addEventListener('DOMContentLoaded', function() {
    console.log('script.js loaded');
    const animateElements = document.querySelectorAll('.animate, .delay-1, .delay-2');
    console.log('Found', animateElements.length, 'elements to animate');

    // Fallback: Make elements visible after 2 seconds if observer doesn't trigger
    setTimeout(() => {
        animateElements.forEach(element => {
            if (element.style.opacity === '0') {
                console.log('Fallback triggered for element:', element);
                element.style.opacity = 1;
                element.style.transform = 'translateY(0)';
            }
        });
    }, 2000);

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                console.log('Element in view:', entry.target);
                entry.target.style.opacity = 1;
                entry.target.style.transform = 'translateY(0)';
                observer.unobserve(entry.target); // Stop observing once animated
            }
        });
    }, {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px' // Adjust to trigger earlier
    });

    animateElements.forEach(element => {
        element.style.opacity = 0;
        element.style.transform = 'translateY(20px)';
        element.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        observer.observe(element);
    });
});