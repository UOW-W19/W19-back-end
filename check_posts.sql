-- Check what posts exist and their statuses
SELECT id, content, status, original_language, author_id, created_at 
FROM posts 
ORDER BY created_at DESC 
LIMIT 10;

-- Check if any posts have null status
SELECT COUNT(*) as null_status_count 
FROM posts 
WHERE status IS NULL;

-- Check status distribution
SELECT status, COUNT(*) as count 
FROM posts 
GROUP BY status;
