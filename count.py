import datetime
import os

def change_day(): 
  return datetime.timedelta(days=1) 

def change_time(): 
  return datetime.timedelta(minutes=1) 

now = datetime.date(2020, 11, 1)
commit_date = datetime.date(2019, 12, 6)
 
while commit_date < now: 
  commit_date = commit_date + change_day() 
  for i in range(5): 
    f = open('data.txt', 'a+') 
    commit_date = commit_date + change_time() 
    f.writelines(commit_date.isoformat() + '\n') 
    f.close()
    os.system('git add .') 
    os.system('git commit --date={time} -m "Update {time}"'.format(time=commit_date.isoformat()))

os.system('git push')