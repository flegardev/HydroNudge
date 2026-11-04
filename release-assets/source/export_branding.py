"""Render original HydroNudge vectors. Requires Pillow + aggdraw for exports only.
Run from project root: python release-assets/source/export_branding.py
No fonts or external artwork are bundled. Segoe UI comes from the local Windows installation.
"""
from pathlib import Path
import re,json,hashlib,io
ROOT=Path(__file__).resolve().parents[2]
from PIL import Image,ImageDraw,ImageFont,ImageCms
import aggdraw
OUT=ROOT/'release-assets'
BG='#061522';SURFACE='#102B3C';AQUA='#7EDCEB';BLUE='#57B8F1';TEXT='#F1FAFF'
PATHS=[('tidal-bowl',AQUA,'M 80 34 C 73 26 63 23 52 25 C 37 27 27 40 28 55 C 29 70 40 81 55 81 C 67 81 77 74 81 63 C 75 69 67 70 59 66 C 51 62 45 55 36 56 C 34 47 38 39 45 35 C 54 30 65 29 80 34 Z'),('rising-current',BLUE,'M 45 47 C 53 40 63 42 70 45 C 76 48 81 47 84 43 C 85 52 82 59 76 62 C 69 65 61 61 55 56 C 52 53 49 49 45 47 Z')]
PROFILE=(ROOT/'release-assets/source/sRGB.icc').read_bytes()
def svg(monochrome=False,background=False):
 body=(f'<rect width="108" height="108" fill="{BG}"/>' if background else '')
 body+=''.join(f'<path id="{name}" fill="{TEXT if monochrome else color}" d="{d}"/>' for name,color,d in PATHS)
 return f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" width="108" height="108">{body}</svg>\n'
def pathdraw(image,scale=1,offset=(0,0),mono=False):
 ctx=aggdraw.Draw(image)
 for name,color,data in PATHS:
  tokens=re.findall(r'[MCZ]|-?\d+(?:\.\d+)?',data);i=0;p=aggdraw.Path()
  while i<len(tokens):
   command=tokens[i];i+=1
   if command=='Z':p.close();continue
   count=2 if command=='M' else 6;v=list(map(float,tokens[i:i+count]));i+=count
   v=[value*scale+offset[j%2] for j,value in enumerate(v)]
   if command=='M':p.moveto(*v)
   else:p.curveto(*v)
  ctx.path(p,aggdraw.Brush(TEXT if mono else color))
 ctx.flush()
def icon(size,background=True,mono=False):
 s=4;im=Image.new('RGBA',(size*s,size*s),BG if background else (0,0,0,0));pathdraw(im,size*s/108,mono=mono);return im.resize((size,size),Image.Resampling.LANCZOS)
def save(im,path):
 im.save(path,icc_profile=PROFILE,optimize=True)
def font(size,bold=False):return ImageFont.truetype('C:/Windows/Fonts/segoeuib.ttf' if bold else 'C:/Windows/Fonts/segoeui.ttf',size)
for name,mono,bg in [('hydronudge-mark.svg',False,False),('hydronudge-icon.svg',False,True),('hydronudge-monochrome.svg',True,False)]:
 (OUT/'source'/name).write_text(svg(mono,bg),encoding='utf-8')
(OUT/'source/brand-geometry.json').write_text(json.dumps({'viewport':108,'background':BG,'paths':PATHS,'safe_zone':'Centered circle diameter66 at54,54; artwork fits within radius33'},indent=2),encoding='utf-8')
save(icon(512),OUT/'icons/play-store-icon-512.png')
save(icon(1080,False),OUT/'icons/adaptive-foreground-1080.png')
save(icon(1080,False,True),OUT/'icons/monochrome-1080.png')
save(Image.new('RGB',(1080,1080),BG),OUT/'icons/adaptive-background-1080.png')
# Android resources, exact geometry shared with the SVGs; no raster rescaling source.
res=ROOT/'app/src/main/res'
for mono,name in [(False,'ic_launcher_foreground.xml'),(True,'ic_launcher_monochrome.xml')]:
 body='\n'.join(f'    <path android:fillColor="{TEXT if mono else color}" android:pathData="{d}" />' for _,color,d in PATHS)
 (res/'drawable'/name).write_text(f'<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="108dp" android:height="108dp" android:viewportWidth="108" android:viewportHeight="108">\n{body}\n</vector>\n',encoding='utf-8')
(res/'drawable/ic_launcher_background.xml').write_text(f'<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle"><solid android:color="{BG}" /></shape>\n',encoding='utf-8')
for name in ['ic_launcher.xml','ic_launcher_round.xml']:
 p=res/'mipmap-anydpi-v26'/name;s=p.read_text(encoding='utf-8');s=re.sub(r'<monochrome[^>]+/>','<monochrome android:drawable="@drawable/ic_launcher_monochrome" />',s);p.write_text(s,encoding='utf-8')
for density,size in [('mdpi',48),('hdpi',72),('xhdpi',96),('xxhdpi',144),('xxxhdpi',192)]:
 for name in ['ic_launcher','ic_launcher_round']:
  im=icon(size)
  if name.endswith('_round'):
   mask=Image.new('L',(size*4,size*4));ImageDraw.Draw(mask).ellipse((0,0,size*4-1,size*4-1),fill=255);im.putalpha(mask.resize((size,size),Image.Resampling.LANCZOS))
  im.save(res/f'mipmap-{density}/{name}.webp',format='WEBP',lossless=True,quality=100,method=6)
  save(im,OUT/f'icons/launcher-{density}-{name}.png')
# Feature: restrained field, large logotype, a small mark; no icon duplication, rankings or unsupported claims.
S=3;im=Image.new('RGB',(1024*S,500*S),BG);d=ImageDraw.Draw(im)
# Original shallow contour rings, low contrast and clipped at the right edge.
for radius in (170,230,290):d.ellipse(((900-radius)*S,(305-radius)*S,(900+radius)*S,(305+radius)*S),outline='#17394A',width=2*S)
pathdraw(im,1.15*S,(735*S,140*S))
d.text((78*S,170*S),'HydroNudge',font=font(68*S,True),fill=TEXT)
d.text((82*S,270*S),'Water, in your rhythm.',font=font(28*S),fill=AQUA)
im=im.resize((1024,500),Image.Resampling.LANCZOS);save(im,OUT/'feature-graphic/hydronudge-feature-1024x500.png')
paths=''.join(f'<path fill="{c}" d="{p}"/>' for _,c,p in PATHS)
feature=f'''<svg xmlns="http://www.w3.org/2000/svg" width="1024" height="500" viewBox="0 0 1024 500"><rect width="1024" height="500" fill="{BG}"/><g stroke="#17394A" fill="none" stroke-width="2"><circle cx="900" cy="305" r="170"/><circle cx="900" cy="305" r="230"/><circle cx="900" cy="305" r="290"/></g><g transform="translate(735 140) scale(1.15)">{paths}</g><text x="78" y="243.667" font-family="Segoe UI, sans-serif" font-weight="700" font-size="68" fill="{TEXT}">HydroNudge</text><text x="82" y="300.333" font-family="Segoe UI, sans-serif" font-size="28" fill="{AQUA}">Water, in your rhythm.</text></svg>'''
(OUT/'source/feature-graphic.svg').write_text(feature,encoding='utf-8')
# Review sheet: masks, small sizes and monochrome palettes. Not an upload asset.
review=Image.new('RGB',(1400,960),'#F1F7FA');d=ImageDraw.Draw(review);d.text((50,30),'HydroNudge / tidal mark',font=font(38,True),fill=BG)
for j,(label,color,maskshape,mono) in enumerate([('Full square',BG,None,False),('Circle',BG,'circle',False),('Squircle',BG,'squircle',False),('Themed dark','#102B3C','circle',True),('Themed light','#D5E8F0','circle',True)]):
 x=50+j*270;ico=icon(220,True,mono)
 if mono:
  ico=Image.new('RGBA',(220,220),color);m=icon(220,False,True).getchannel('A');fill=Image.new('RGBA',(220,220),AQUA if j==3 else '#126A90');ico.alpha_composite(Image.composite(fill,Image.new('RGBA',(220,220)),m))
 if maskshape:
  mask=Image.new('L',(880,880));md=ImageDraw.Draw(mask)
  if maskshape=='circle':md.ellipse((0,0,879,879),fill=255)
  else:md.rounded_rectangle((0,0,879,879),radius=220,fill=255)
  ico.putalpha(mask.resize((220,220),Image.Resampling.LANCZOS))
 review.paste(ico,(x,130),ico);d.text((x,370),label,font=font(24),fill=BG)
for j,size in enumerate([24,32,48,72,96,144,192]):
 x=50+j*185;im2=icon(size);review.paste(im2,(x,490),im2);d.text((x,710),str(size)+' px',font=font(22),fill=BG)
# Geometry proof, white circle radius33 centered in108 viewport.
geo=icon(240);g=ImageDraw.Draw(geo);k=240/108;g.ellipse((21*k,21*k,87*k,87*k),outline='white',width=1);review.paste(geo,(1050,690),geo)
save(review,OUT/'review/icon-contact-sheet.png')
print('Original SVGs, store icon, feature graphic,10 Android fallback bitmaps and adaptive/monochrome vectors exported')
