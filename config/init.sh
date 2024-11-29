#!/bin/bash
# Initializing RabbitMQ exchange and queue

# Download rabbitmqadmin if not already available
if ! [ -f /usr/local/bin/rabbitmqadmin ]; then
  echo "Downloading rabbitmqadmin..."
  curl -o /usr/local/bin/rabbitmqadmin https://raw.githubusercontent.com/rabbitmq/rabbitmq-management/rabbitmq_management-3.8.x/cli/rabbitmqadmin
  chmod +x /usr/local/bin/rabbitmqadmin
fi

# Wait until RabbitMQ is ready
rabbitmqctl wait /var/lib/rabbitmq/mnesia/rabbitmq\@$(hostname)

# Declare the exchange
rabbitmqadmin -u com-movie-recommender-user -p com-movie-recommender-password  declare exchange name=location.exchange type=direct

# Declare the queue
rabbitmqadmin -u com-movie-recommender-user -p com-movie-recommender-password  declare queue name=location.update.queue durable=true

# Bind the queue to the exchange
rabbitmqadmin -u com-movie-recommender-user -p com-movie-recommender-password  declare binding source=location.exchange destination=location.update.queue routing_key=location.update
