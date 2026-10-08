document.addEventListener('DOMContentLoaded', () => {
    const launcher = document.getElementById('chatLauncher');
    const panel = document.getElementById('chatPanel');
    const closeBtn = document.getElementById('chatCloseBtn');
    const input = document.getElementById('chatInput');
    const sendBtn = document.getElementById('chatSendBtn');
    const messagesContainer = document.getElementById('chatMessages');
    const chips = document.querySelectorAll('.chat-chip');

    if (!launcher || !panel) return;

    // Toggle chat panel visibility
    launcher.addEventListener('click', () => {
        panel.style.display = (panel.style.display === 'flex') ? 'none' : 'flex';
        if (panel.style.display === 'flex') {
            input.focus();
        }
    });

    closeBtn.addEventListener('click', () => {
        panel.style.display = 'none';
    });

    // Send on button click or Enter key
    sendBtn.addEventListener('click', () => sendMessage());
    input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

    // Quick chips handler
    chips.forEach(chip => {
        chip.addEventListener('click', () => {
            const question = chip.getAttribute('data-question');
            if (question) {
                input.value = question;
                sendMessage();
            }
        });
    });

    async function sendMessage() {
        const text = input.value.trim();
        if (!text) return;

        // Render user message
        appendMessage(text, 'user');
        input.value = '';

        // Render loading state
        const loadingMsg = appendMessage('Typing response...', 'bot', true);

        try {
            const contextPath = window.APP_CONTEXT_PATH || '';
            const response = await fetch(contextPath + '/api/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ message: text })
            });

            const result = await response.json();
            loadingMsg.remove();

            if (result.success && result.data) {
                const replyText = result.data.reply;
                const cachedBadge = result.data.cached ? '⚡ Cached' : '';
                appendMessage(replyText, 'bot', false, cachedBadge);
            } else {
                appendMessage(result.error || 'Sorry, I could not process your request.', 'bot');
            }
        } catch (error) {
            loadingMsg.remove();
            appendMessage('Connection error. Please verify network or try again.', 'bot');
        }
    }

    function appendMessage(text, sender, isLoading = false, meta = '') {
        const msgDiv = document.createElement('div');
        msgDiv.className = `chat-msg chat-msg-${sender}`;
        msgDiv.textContent = text;

        if (isLoading) {
            msgDiv.style.opacity = '0.6';
            msgDiv.style.fontStyle = 'italic';
        }

        if (meta && !isLoading) {
            const metaDiv = document.createElement('div');
            metaDiv.className = 'chat-meta';
            metaDiv.textContent = meta;
            msgDiv.appendChild(metaDiv);
        }

        messagesContainer.appendChild(msgDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
        return msgDiv;
    }
});
