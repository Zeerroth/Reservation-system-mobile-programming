# 🚌✈️ Online Rezervasyon Sistemi - Android App

Modern, kullanıcı dostu otobüs ve uçak bileti rezervasyon uygulaması.

## 📱 Özellikler

### Kullanıcı Özellikleri
- ✅ Kullanıcı kaydı ve girişi
- 🔍 Gelişmiş sefer arama ve filtreleme
  - Şirket, şehir, tarih, fiyat ve ulaşım türü filtreleme
- 🎫 İnteraktif koltuk seçimi
- 📋 Rezervasyon yönetimi (iptal, paylaşma)
- 👤 Profil yönetimi
- 📊 Gerçek zamanlı müsait koltuk gösterimi

### Admin Özellikleri
- ➕ Sefer ekleme/silme
- 📝 Detaylı sefer bilgileri (kalkış, varış, süre)
- 🎛️ Kapsamlı yönetim paneli

### Teknik Özellikler
- 🏗️ MVVM mimarisi
- 💾 Room Database
- 🎨 Material Design 3
- 🔄 Flow & Coroutines
- 📱 Ekran rotasyonu desteği
- 🎯 RecyclerView ile optimize liste görüntüleme

---

## 🛠️ Kurulum Talimatları

### Gereksinimler

1. **Android Studio** (En son sürüm önerilir - Hedgehog veya üzeri)
   - [İndir](https://developer.android.com/studio)
   
2. **JDK 17** veya üzeri
   - Android Studio ile birlikte gelir

3. **Git** (Projeyi klonlamak için)
   - [İndir](https://git-scm.com/downloads)

---

### Adım Adım Kurulum

#### 1. Projeyi Klonlayın

```bash
git clone https://github.com/Zeerroth/Reservation-system-mobile-programming.git
cd Reservation-system-mobile-programming
```

#### 2. Android Studio'da Açın

1. **Android Studio'yu başlatın**
2. **File** → **Open** (veya açılış ekranında **Open**)
3. Klonladığınız klasörü seçin
4. **OK**'e tıklayın

#### 3. Gradle Sync

- Android Studio otomatik olarak Gradle sync yapacak
- **Alt** kısımda "Sync" progress bar'ı görünecek
- İlk seferde biraz zaman alabilir (bağımlılıkları indiriyor)
- Eğer otomatik başlamazsa: **File** → **Sync Project with Gradle Files**

#### 4. Emulator Kurulumu (Fiziksel cihaz yoksa)

1. **Tools** → **Device Manager**
2. **Create Device** butonuna tıklayın
3. Bir cihaz seçin (örn: **Pixel 5**)
4. **Next** → Bir sistem görüntüsü seçin (örn: **API 34 - Android 14**)
5. **Next** → **Finish**

#### 5. Uygulamayı Çalıştırın

1. Üst toolbar'da cihazınızı seçin (emulator veya fiziksel cihaz)
2. Yeşil **▶️ Run** butonuna tıklayın (veya **Shift + F10**)
3. İlk build biraz zaman alabilir
4. Uygulama açılacak!

---

## 👤 Demo Hesapları

### Kullanıcı Hesabı
```
Email: user1@test.com
Şifre: user123
```

### Admin Hesabı
```
Email: admin@test.com
Şifre: admin123
```

---

## 📂 Proje Yapısı

```
app/
├── src/main/
│   ├── java/com/example/rezervasyon/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── dao/          # Database erişim katmanı
│   │   │   │   ├── database/     # Room Database
│   │   │   │   └── entities/     # Veri modelleri
│   │   ├── ui/
│   │   │   ├── admin/            # Admin paneli
│   │   │   ├── auth/             # Giriş/Kayıt
│   │   │   ├── main/             # Ana ekran
│   │   │   ├── profile/          # Profil
│   │   │   ├── reservations/     # Rezervasyonlar
│   │   │   ├── seat/             # Koltuk seçimi
│   │   │   ├── splash/           # Splash ekran
│   │   │   └── trips/            # Sefer listesi
│   │   └── utils/                # Yardımcı sınıflar
│   └── res/                      # Kaynaklar (layout, drawable, vb.)
```

---

## 🚀 Kullanım

### İlk Çalıştırma

1. Uygulama ilk açılışta **örnek verilerle** doldurulur
2. Splash ekrandan sonra **Login** ekranı açılır
3. Demo hesaplardan biriyle giriş yapın

### Sefer Arama

1. **Seferler** sekmesine gidin
2. **Filtreler** butonuna tıklayın
3. İstediğiniz kriterleri seçin:
   - Ulaşım türü (Otobüs/Uçak)
   - Şirket
   - Kalkış/Varış şehri
   - Tarih
   - Fiyat aralığı
4. **Uygula** butonuna basın

### Rezervasyon Yapma

1. Bir sefer kartına tıklayın
2. Müsait koltukları seçin (yeşil)
3. **Rezervasyonu Onayla** butonuna basın
4. **Rezervasyonlarım** sekmesinden kontrol edin

### Admin İşlemleri

1. Admin hesabıyla giriş yapın
2. **Profil** → **Admin Paneli**
3. Yeni sefer ekleyin veya mevcut seferleri silin

---

## 🔧 Sorun Giderme

### Build Hatası

**Hata:** `Gradle sync failed`
```bash
# Çözüm:
File → Invalidate Caches → Invalidate and Restart
```

### Veritabanı Hatası

**Hata:** `DatabaseVersionException` veya eski data
```bash
# Çözüm (Emulator):
Settings → Apps → Rezervasyon → Storage → Clear Data
```

### Emulator Yavaş

```bash
# Çözüm:
1. Device Manager'da emulator'u sil
2. Yeni bir emulator oluştur
3. "Hardware - GLES 2.0" seçeneğini etkinleştir
```

---

## 🎨 Teknolojiler

- **Kotlin** - Programlama dili
- **MVVM** - Mimari pattern
- **Room** - Lokal veritabanı
- **Coroutines & Flow** - Asenkron işlemler
- **Material Design 3** - UI komponenleri
- **View Binding** - View erişimi
- **RecyclerView** - Liste görüntüleme

---

## 📄 Lisans

Bu proje eğitim amaçlıdır.

---

## 🤝 Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit edin (`git commit -m 'Add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request açın

---

## 📞 İletişim

Proje Sahibi: Zeerroth
Repository: [Reservation-system-mobile-programming](https://github.com/Zeerroth/Reservation-system-mobile-programming)

---

**⭐ Projeyi beğendiyseniz yıldız vermeyi unutmayın!**
