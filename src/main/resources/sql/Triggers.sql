-- Cron job to update monthly tokens
SELECT cron.schedule('0 0 1 * *', $$UPDATE users SET monthlytokens = 1;$$);


-- Cron job to update tasks every 12 hours
SELECT cron.schedule('0 */12 * * *',$$UPDATE task SET status = 'UNCOMPLETED' WHERE status != 'DONE' AND duedate = CURRENT_DATE::varchar;$$);

-- Verify the cron job has been created
SELECT * FROM cron.job;