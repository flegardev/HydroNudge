"""Compose genuine app captures into Play phone artwork; never redraw app UI.
Genuine raw crops and their manifest are retained in release-assets/minimal-update.
--from-raw rebuilds the artwork entirely from those retained crops.
"""
from pathlib import Path
import sys,json,hashlib
ROOT=Path(__file__).resolve().parents[2]
from PIL import Image,ImageDraw,ImageFont,ImageCms
ASSET=ROOT/'release-assets/minimal-update';CAP=ROOT/'qa-reports/closed-test'
for folder in ['phone-screenshots','raw-screenshots','review']:(ASSET/folder).mkdir(parents=True,exist_ok=True)
PROFILE=(ROOT/'release-assets/source/sRGB.icc').read_bytes()
SPEC=[('01-today','Your water.','A calmer rhythm.','Today dashboard with staged 1250 mL of water, a 2500 mL goal and four quick-add buttons.'),('02-quick-log','One tap.','Water logged.','Actual dashboard after adding 250 mL, with 1500 mL recorded and a View all link for the four entries.'),('03-history','Your week,','in view.','Actual seven-day history, recorded-day averages and daily chart using staged water entries.'),('04-custom-entry','Your amount.','Your moment.','Actual custom water entry dialog with an unsaved 375 mL amount, date, time and optional note.'),('05-settings','Less clutter.','More focus.','Actual Settings screen with daily goal, units, appearance choices and optional water reminders.')]

def font(size,bold=False):return ImageFont.truetype('C:/Windows/Fonts/segoeuib.ttf' if bold else 'C:/Windows/Fonts/segoeui.ttf',size)
def save(im,p):im.save(p,icc_profile=PROFILE,optimize=True)
records=[]
for i,(name,line1,line2,alt) in enumerate(SPEC,1):
 native=CAP/(name+'.png');rawpath=ASSET/'raw-screenshots'/(name+'-app-crop.png')
 prior={Path(x['file']).stem:x for x in json.loads((ASSET/'screenshot-manifest.json').read_text(encoding='utf-8'))} if (ASSET/'screenshot-manifest.json').exists() else {}
 if '--from-raw' in sys.argv or not native.exists():
  raw=Image.open(rawpath).convert('RGB');crop=tuple(prior[name]['crop']);native_hash=prior[name]['native_source_sha256']
 else:
  im=Image.open(native).convert('RGB');assert im.size==(1080,1920),(name,im.size)
  if name=='06-widget':crop=tuple(json.loads((CAP/'widget-crop.json').read_text(encoding='utf-8'))['crop'])
  elif name=='05-reminders':crop=(0,112,1080,1640)
  else:crop=(0,112,1080,1872)
  raw=im.crop(crop);native_hash=hashlib.sha256(native.read_bytes()).hexdigest()
  if name=='06-widget':
   # Decorative rounded crop removes wallpaper only; all widget text/controls stay intact.
   mask=Image.new('L',(raw.width*4,raw.height*4),0);ImageDraw.Draw(mask).rounded_rectangle((0,0,mask.width-1,mask.height-1),radius=82*4,fill=255);mask=mask.resize(raw.size,Image.Resampling.LANCZOS);raw=Image.composite(raw,Image.new('RGB',raw.size,'#061522'),mask)
  save(raw,rawpath)
 board=Image.new('RGB',(1080,1920),'#061522');d=ImageDraw.Draw(board)
 mark=Image.open(ROOT/'release-assets/icons/adaptive-foreground-1080.png').convert('RGBA').resize((75,75),Image.Resampling.LANCZOS)
 board.paste(mark,(75,42),mark);d.text((160,54),'HydroNudge',font=font(34),fill='#ACC7D4')
 d.text((90,123),line1,font=font(66,True),fill='#F1FAFF');d.text((90,203),line2,font=font(66,True),fill='#7EDCEB')
 if name=='06-widget':
  # Real widget image remains intact; it is not reconstructed or given fictitious controls.
  width=940;height=round(raw.height*width/raw.width);view=raw.resize((width,height),Image.Resampling.LANCZOS);x=(1080-width)//2;y=660
  d.ellipse((60,450,1020,1410),outline='#17394A',width=2)
  d.ellipse((-60,330,1140,1530),outline='#102B3C',width=2)
  board.paste(view,(x,y));d.text((90,1330),'Your total. Your quick-add buttons.',font=font(33),fill='#ACC7D4')
 else:
  width=900;height=round(raw.height*width/raw.width);view=raw.resize((width,height),Image.Resampling.LANCZOS);x=90;y=345
  mask=Image.new('L',view.size,0);ImageDraw.Draw(mask).rounded_rectangle((0,0,width-1,height-1),radius=30,fill=255)
  board.paste(view,(x,y),mask);d.rounded_rectangle((x-1,y-1,x+width,y+height),radius=31,outline='#17394A',width=2)
 if name=='05-reminders':d.text((90,1710),'Quiet hours. Flexible intervals.',font=font(33),fill='#ACC7D4')
 d.text((90,1850),f'{i:02d} / {len(SPEC):02d}',font=font(23),fill='#829DAD')
 output=ASSET/'phone-screenshots'/(name+'.png');save(board,output)
 records.append({'file':output.relative_to(ASSET).as_posix(),'dimensions':[1080,1920],'mode':'RGB','alt_text':alt,'native_source_sha256':native_hash,'crop':list(crop),'raw_crop':rawpath.relative_to(ASSET).as_posix(),'raw_crop_sha256':hashlib.sha256(rawpath.read_bytes()).hexdigest(),'sha256':hashlib.sha256(output.read_bytes()).hexdigest(),'staged_demonstration_data':True,'widget_rounded_crop_radius_source_px':82 if name=='06-widget' else None,'composition':'Original captions and ocean field around a real cropped app capture; no app UI reconstructed'})
(ASSET/'screenshot-manifest.json').write_text(json.dumps(records,indent=2,ensure_ascii=False),encoding='utf-8')
# All seven final exports on a readable review contact sheet.
sheet=Image.new('RGB',(len(SPEC)*324,650),'#102B3C');d=ImageDraw.Draw(sheet)
for i,r in enumerate(records):
 thumbnail=Image.open(ASSET/r['file']).resize((304,540),Image.Resampling.LANCZOS);sheet.paste(thumbnail,(i*324+10,15));d.text((i*324+12,575),Path(r['file']).stem,font=font(21),fill='#F1FAFF')
save(sheet,ASSET/'review/phone-contact-sheet.png')
print('Exported five current authentic phone screenshots')
