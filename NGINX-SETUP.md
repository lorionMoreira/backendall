# Nginx Reverse Proxy Setup

## Installation

### Ubuntu/Debian
```bash
sudo apt update
sudo apt install nginx
```

### CentOS/RHEL
```bash
sudo yum install nginx
```

## Configuration

### 1. Copy the configuration file

```bash
# Copy to sites-available
sudo cp nginx.conf /etc/nginx/sites-available/backend

# Create symbolic link to sites-enabled
sudo ln -s /etc/nginx/sites-available/backend /etc/nginx/sites-enabled/

# Or directly to conf.d (alternative method)
sudo cp nginx.conf /etc/nginx/conf.d/backend.conf
```

### 2. Test configuration

```bash
sudo nginx -t
```

### 3. Restart Nginx

```bash
# Restart
sudo systemctl restart nginx

# Or reload (zero downtime)
sudo systemctl reload nginx

# Enable on boot
sudo systemctl enable nginx
```

### 4. Check status

```bash
sudo systemctl status nginx
```

## Firewall Configuration

### UFW (Ubuntu)
```bash
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw status
```

### Firewalld (CentOS)
```bash
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload
```

## Testing

```bash
# Test from local machine
curl http://localhost/api/protected/test

# Test with headers
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost/api/protected/hello
```

## Logs

### View access logs
```bash
sudo tail -f /var/log/nginx/backend_access.log
```

### View error logs
```bash
sudo tail -f /var/log/nginx/backend_error.log
```

### View all Nginx logs
```bash
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

## SSL/HTTPS Setup (Optional)

### Using Let's Encrypt (Certbot)

```bash
# Install certbot
sudo apt install certbot python3-certbot-nginx

# Get certificate (replace with your domain)
sudo certbot --nginx -d yourdomain.com

# Auto-renewal test
sudo certbot renew --dry-run
```

### Manual SSL Configuration

Add to your nginx.conf:

```nginx
server {
    listen 443 ssl http2;
    server_name yourdomain.com;

    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;
    
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Rest of your configuration...
    location / {
        proxy_pass http://localhost:8080;
        # ... other proxy settings
    }
}

# Redirect HTTP to HTTPS
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}
```

## Troubleshooting

### Check if Nginx is running
```bash
sudo systemctl status nginx
```

### Check if port 80 is in use
```bash
sudo netstat -tulpn | grep :80
# or
sudo ss -tulpn | grep :80
```

### Check if backend is running
```bash
curl http://localhost:8080/actuator/health
```

### Permission issues
```bash
# Check SELinux status (CentOS/RHEL)
sestatus

# Allow Nginx to connect to network
sudo setsebool -P httpd_can_network_connect 1
```

### Configuration test failed
```bash
# Detailed syntax check
sudo nginx -t -c /etc/nginx/nginx.conf

# Check for typos in config file
sudo cat /etc/nginx/sites-available/backend
```

## Performance Tuning (Optional)

Add to your nginx.conf for better performance:

```nginx
# Enable gzip compression
gzip on;
gzip_vary on;
gzip_min_length 1024;
gzip_types text/plain text/css application/json application/javascript text/xml application/xml;

# Connection pool
keepalive_timeout 65;
keepalive_requests 100;

# Buffer sizes
proxy_buffer_size 4k;
proxy_buffers 8 4k;
proxy_busy_buffers_size 8k;
```

## Access Points

After setup:
- **API**: http://localhost/api/
- **Swagger**: http://localhost/swagger-ui.html
- **Health**: http://localhost/health

## Useful Commands

```bash
# Reload configuration without downtime
sudo nginx -s reload

# Stop Nginx gracefully
sudo nginx -s quit

# Stop Nginx immediately
sudo nginx -s stop

# Reopen log files
sudo nginx -s reopen

# Test configuration
sudo nginx -t

# Show Nginx version
nginx -v

# Show Nginx version and configuration
nginx -V
```
