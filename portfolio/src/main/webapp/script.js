(function ($) {
    "use strict"; // Start of use strict

    // Smooth scrolling using jQuery easing
    $('a.js-scroll-trigger[href*="#"]:not([href="#"])').click(function () {
        if (
            location.pathname.replace(/^\//, "") ==
                this.pathname.replace(/^\//, "") &&
            location.hostname == this.hostname
        ) {
            var target = $(this.hash);
            target = target.length
                ? target
                : $("[name=" + this.hash.slice(1) + "]");
            if (target.length) {
                $("html, body").animate(
                    {
                        scrollTop: target.offset().top,
                    },
                    1000,
                    "easeInOutExpo"
                );
                return false;
            }
        }
    });

    // Closes responsive menu when a scroll trigger link is clicked
    $(".js-scroll-trigger").click(function () {
        $(".navbar-collapse").collapse("hide");
    });

    // Activate scrollspy to add active class to navbar items on scroll
    $("body").scrollspy({
        target: "#sideNav",
    });
})(jQuery); // End of use strict

/** 
 * Creates an <li> element containing text.
 * @param text The value of the element.
 * @return The list-element.
 */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}

function getMessages() {
    fetch('/data')  // sends a request to /my-data-url
        .then(response => response.json()) // parses the response as JSON
        .then((comments) => { // now we can reference the fields in myObject!
            const commentsListElement = document.getElementById('messages-container');
            commentsListElement.innerHTML = '';
            for (let index in comments) {
                commentsListElement.appendChild(createListElement(comments[index].name+' says '+comments[index].content));
            }
        });
}