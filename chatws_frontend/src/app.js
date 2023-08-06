const socket = new WebSocket('ws://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, (frame) => {
    console.log('WebSocket connection established.');

    // Subscribe to the "/topic/messages" topic to receive messages from the server
    stompClient.subscribe('/topic/messages', (response) => {
        const message = JSON.parse(response.body);
        displayMessage(message);
    });

    // Request previous messages from the server when a new client connects
    stompClient.send('/app/getPreviousMessages', {}, '');
});

function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const username = formData.get("username");
    const content = formData.get("content");

    const message = {
        username,
        content
    };

    stompClient.send('/app/chat', {}, JSON.stringify(message));
    event.target.reset();
}

function displayMessage({ username, content }) {
    const chatArea = document.getElementById('chatArea');

    chatArea.innerHTML += `${username}: ${content}` + '<br>';
}
