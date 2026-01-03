//
//  BaseHelper.swift
//  CommonKit
//
//  Created by derTurke on 24.03.2025.
//

import UIKit
import Photos
import DesignKit
import AVFoundation
import Metal

public enum TypeOfPermission: String {
    case camera = "Kamera"
    case gallery = "Galeri"
    case location = "Konum"
}

public enum TypeOfAsset {
    case image
    case video
}

public final class BaseHelper {
    public static let shared = BaseHelper()
    
    private init() {}
    
    public func currentViewController() -> UIViewController? {
        guard let windowScene = UIApplication.shared.connectedScenes
            .first(where: { $0.activationState == .foregroundActive }) as? UIWindowScene else {
            return nil
        }
        
        guard let window = windowScene.windows.first(where: { $0.isKeyWindow }) else {
            return nil
        }
        
        var currentVC = window.rootViewController
        
        while let presentedVC = currentVC?.presentedViewController {
            currentVC = presentedVC
        }
        
        if let tabBarVC = currentVC as? UITabBarController {
            currentVC = tabBarVC.selectedViewController
        }
        
        if let navigationVC = currentVC as? UINavigationController {
            currentVC = navigationVC.visibleViewController
        }
        
        return currentVC
    }
    
    @MainActor
    public func navigationControllerToContains(with viewController: UIViewController.Type) -> Bool {
        guard let viewControllers = currentViewController()?.navigationController?.viewControllers.reversed() else {
            return false
        }
        
        return viewControllers.contains { $0.isKind(of: viewController) }
    }
    
    
    @MainActor
    public func showIndicator() {
        guard let currentVC = currentViewController() else { return }
        
        DispatchQueue.main.async {
            if currentVC.view.viewWithTag(9998) != nil { return }
            
            let blockerView = UIView(frame: currentVC.view.bounds)
            blockerView.backgroundColor = UIColor.black.withAlphaComponent(0.3)
            blockerView.tag = 9998
            blockerView.isUserInteractionEnabled = true
            
            let indicatorView = UIActivityIndicatorView(style: .large)
            indicatorView.color = DesignKit.ColorName.backgroundPrimaryGreen.color
            indicatorView.center = blockerView.center
            indicatorView.tag = 9999
            indicatorView.startAnimating()
            
            blockerView.addSubview(indicatorView)
            currentVC.view.addSubview(blockerView)
        }
    }
    
    @MainActor
    public func hideIndicator() {
        guard let currentVC = currentViewController() else { return }
        
        DispatchQueue.main.async {
            if let blockerView = currentVC.view.viewWithTag(9998) {
                if let indicatorView = blockerView.viewWithTag(9999) as? UIActivityIndicatorView {
                    indicatorView.stopAnimating()
                }
                blockerView.removeFromSuperview()
            }
        }
    }
    
    public func checkCameraPermission(completion: @escaping (_ authorized: Bool) -> Void) {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            completion(true)
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                guard let self else { return }
                if granted {
                    DispatchQueue.main.async {
                        completion(true)
                    }
                }
            }
        default:
            completion(false)
        }
    }
    
    public func checkPhotoLibraryPermission(completion: @escaping (_ authorized: Bool) -> Void) {
        switch PHPhotoLibrary.authorizationStatus(for: .readWrite) {
        case .authorized:
            completion(true)
        case .limited:
            completion(true)
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization(for: .readWrite) { [weak self] status in
                guard let _ = self else { return }
                if status == .authorized || status == .limited {
                    DispatchQueue.main.async {
                        completion(true)
                    }
                }
            }
        default:
            completion(false)
        }
    }
    
    public func showPermissionAlert(for type: TypeOfPermission) -> UIAlertController {
        let alertController = UIAlertController(
            title: "İzin Gerekli",
            message: "\(type.rawValue) erişimi için ayarlardan izin vermeniz gerekmektedir.",
            preferredStyle: .alert
        )
        
        let settingsAction = UIAlertAction(title: "Ayarlar", style: .default) { _ in
            if let settingsURL = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(settingsURL)
            }
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(settingsAction)
        alertController.addAction(cancelAction)
        
        return alertController
    }
    
    public func fetchPhotoLibraryAssets() -> [PHAsset] {
        let fetchOptions = PHFetchOptions()
        fetchOptions.predicate = NSPredicate(
            format: "mediaType == %d || mediaType == %d",
            PHAssetMediaType.image.rawValue,
            PHAssetMediaType.video.rawValue
        )
        
        let userLibrary = PHAssetCollection.fetchAssetCollections(with: .smartAlbum, subtype: .smartAlbumUserLibrary, options: nil).firstObject
        
        var assetsArray: [PHAsset] = []
        
        if let collection = userLibrary {
            let assets = PHAsset.fetchAssets(in: collection, options: fetchOptions)
            assets.enumerateObjects { (asset, _, _) in
                assetsArray.append(asset)
            }
        }
        
        return assetsArray
    }
    
    public func requestImage(
        for asset: PHAsset,
        targetSize: CGSize = PHImageManagerMaximumSize
    ) async -> UIImage? {
        await withCheckedContinuation { continuation in
            let options = PHImageRequestOptions()
            options.deliveryMode = .highQualityFormat
            options.isSynchronous = false
            options.isNetworkAccessAllowed = true
            options.resizeMode = .exact
            
            PHImageManager.default().requestImage(for: asset, targetSize: targetSize, contentMode: .aspectFit, options: options) { image, _ in
                continuation.resume(returning: image)
            }
        }
    }
    
    public func requestVideoAsset(for asset: PHAsset) async -> AVAsset? {
        await withCheckedContinuation { continuation in
            let options = PHVideoRequestOptions()
            options.deliveryMode = .automatic
            options.isNetworkAccessAllowed = true
            
            PHImageManager.default().requestAVAsset(forVideo: asset, options: options) { avAsset, _, _ in
                continuation.resume(returning: avAsset)
            }
        }
    }
    
    public func convertAVAssetToURL(_ asset: AVAsset, size: CGSize? = nil) async -> URL? {
        let outputDirectory = FileManager.default.temporaryDirectory
        let outputURL = outputDirectory.appendingPathComponent(UUID().uuidString).appendingPathExtension("mp4")
        
        guard let exportSession = AVAssetExportSession(asset: asset, presetName: AVAssetExportPresetPassthrough) else {
            return nil
        }
        
        exportSession.outputURL = outputURL
        if let size {
            exportSession.videoComposition = buildVideoComposition(asset: asset, size: size)
        }
        exportSession.outputFileType = .mp4
        
        return await withCheckedContinuation { continuation in
            exportSession.exportAsynchronously {
                if exportSession.status == .completed {
                    continuation.resume(returning: outputURL)
                } else {
                    print("Export failed: \(String(describing: exportSession.error))")
                    continuation.resume(returning: nil)
                }
            }
        }
    }
    
    public func fetchLatestLastAsset(type: TypeOfAsset) async -> PHAsset? {
        let options = PHFetchOptions()
        options.sortDescriptors = [NSSortDescriptor(key: "creationDate", ascending: false)]
        options.fetchLimit = 1
        
        let assets = PHAsset.fetchAssets(with: type == .image ? .image : .video, options: options)
        return assets.firstObject
    }
    
    private func buildVideoComposition(asset: AVAsset, size: CGSize) -> AVMutableVideoComposition {
        let videoComposition = AVMutableVideoComposition()
        videoComposition.renderSize = size
        videoComposition.frameDuration = CMTimeMake(value: 1, timescale: 30)
        
        let instruction = AVMutableVideoCompositionInstruction()
        instruction.timeRange = CMTimeRange(start: .zero, duration: asset.duration)
        
        let transformer = AVMutableVideoCompositionLayerInstruction(assetTrack: asset.tracks(withMediaType: .video).first!)
        instruction.layerInstructions = [transformer]
        
        videoComposition.instructions = [instruction]
        
        return videoComposition
    }

    public func imageWithTextFromLabels(image: UIImage, labels: [UILabel], imageView: UIImageView) -> UIImage {
        let imageViewForRendering = UIImageView(image: image)
        imageViewForRendering.contentMode = .scaleAspectFit
        imageViewForRendering.frame = imageView.bounds
        
        for label in labels {
            imageViewForRendering.addSubview(label)
        }
        
        UIGraphicsBeginImageContextWithOptions(imageViewForRendering.bounds.size, true, 0.0) // Şeffaf arka plan
        defer { UIGraphicsEndImageContext() }
        
        imageViewForRendering.layer.render(in: UIGraphicsGetCurrentContext()!)
        
        guard let finalImage = UIGraphicsGetImageFromCurrentImageContext() else {
            return image // Hata durumunda orijinal resmi döndür
        }
        
        return finalImage
    }

    
    public func crop(image: UIImage,
                     cropRect: CGRect) -> UIImage {
        guard let cgImage = image.cgImage else { return image }
        
        let imageSize = CGSize(width: cgImage.width, height: cgImage.height)
        
        let actualCropRect = CGRect(
            x: cropRect.origin.x * imageSize.width,
            y: cropRect.origin.y * imageSize.height,
            width: cropRect.width * imageSize.width,
            height: cropRect.height * imageSize.height
        ).integral
        
        guard let croppedCGImage = cgImage.cropping(to: actualCropRect) else {
            return image
        }
        
        return UIImage(cgImage: croppedCGImage, scale: image.scale, orientation: image.imageOrientation)
    }
    
    public func cropVideo(inputURL: URL,
                          cropRectInView: CGRect,
                          viewSize: CGSize,
                          completion: @escaping (URL?) -> Void) {
        
        let asset = AVAsset(url: inputURL)
        
        guard let videoTrack = asset.tracks(withMediaType: .video).first else {
            print("No video track found")
            completion(nil)
            return
        }

        // Videonun orijinal boyutu ve transform bilgisi
        let naturalSize = videoTrack.naturalSize
        let preferredTransform = videoTrack.preferredTransform
        let isPortrait = abs(preferredTransform.b) == 1 && abs(preferredTransform.c) == 1
        
        let videoSize: CGSize = isPortrait
            ? CGSize(width: naturalSize.height, height: naturalSize.width)
            : naturalSize

        // ✅ UIView cropRect'ini video piksellerine dönüştür
        func convertViewCropRectToVideoCropRect(viewCropRect: CGRect,
                                                in viewBounds: CGSize,
                                                videoSize: CGSize) -> CGRect {
            let scaleX = videoSize.width / viewBounds.width
            let scaleY = videoSize.height / viewBounds.height
            
            let x = viewCropRect.origin.x * scaleX
            let y = viewCropRect.origin.y * scaleY
            let width = viewCropRect.width * scaleX
            let height = viewCropRect.height * scaleY
            
            return CGRect(x: x, y: y, width: width, height: height).integral
        }

        let actualCropRect = convertViewCropRectToVideoCropRect(
            viewCropRect: cropRectInView,
            in: viewSize,
            videoSize: videoSize
        )

        // 🎥 Kompozisyon oluştur
        let composition = AVMutableComposition()
        guard let videoCompositionTrack = composition.addMutableTrack(withMediaType: .video,
                                                                       preferredTrackID: kCMPersistentTrackID_Invalid) else {
            print("Failed to create composition track")
            completion(nil)
            return
        }

        do {
            try videoCompositionTrack.insertTimeRange(CMTimeRange(start: .zero, duration: asset.duration),
                                                      of: videoTrack,
                                                      at: .zero)
        } catch {
            print("Error inserting time range: \(error)")
            completion(nil)
            return
        }

        // 🎯 Transform: Videoyu cropRect'e göre kaydır
        let transform = preferredTransform.concatenating(
            CGAffineTransform(translationX: -actualCropRect.origin.x, y: -actualCropRect.origin.y)
        )

        let instruction = AVMutableVideoCompositionInstruction()
        instruction.timeRange = CMTimeRange(start: .zero, duration: asset.duration)

        let layerInstruction = AVMutableVideoCompositionLayerInstruction(assetTrack: videoCompositionTrack)
        layerInstruction.setTransform(transform, at: .zero)

        instruction.layerInstructions = [layerInstruction]

        let videoComposition = AVMutableVideoComposition()
        videoComposition.instructions = [instruction]
        videoComposition.renderSize = actualCropRect.size
        videoComposition.frameDuration = CMTime(value: 1, timescale: 30)

        // 📤 Export
        let outputURL = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString + ".mov")

        guard let exporter = AVAssetExportSession(asset: composition,
                                                  presetName: AVAssetExportPresetHighestQuality) else {
            print("Failed to create export session")
            completion(nil)
            return
        }

        exporter.outputURL = outputURL
        exporter.outputFileType = .mov
        exporter.videoComposition = videoComposition
        exporter.shouldOptimizeForNetworkUse = true

        exporter.exportAsynchronously {
            switch exporter.status {
            case .completed:
                print("✅ Export succeeded: \(outputURL)")
                completion(outputURL)
            case .failed:
                print("❌ Export failed: \(exporter.error?.localizedDescription ?? "Unknown error")")
                completion(nil)
            default:
                completion(nil)
            }
        }
    }

    
    public func openMap(latitude: Double, longitude: Double) {
        let googleMapsURL = URL(string: "comgooglemaps://?q=\(latitude),\(longitude)&zoom=14")!
        let appleMapsURL = URL(string: "http://maps.apple.com/?ll=\(latitude),\(longitude)")!
        
        if UIApplication.shared.canOpenURL(googleMapsURL) {
            UIApplication.shared.open(googleMapsURL, options: [:], completionHandler: nil)
        } else {
            UIApplication.shared.open(appleMapsURL, options: [:], completionHandler: nil)
        }
    }
    
    public func findTargetViewController(
        _ targetVCType: UIViewController.Type,
        isReversed: Bool = true
    ) -> (vc: UIViewController, nav: UINavigationController)? {

        guard let topVC = currentViewController() else { return nil }

        let searchLists: [[UIViewController]?] = [
            topVC.navigationController?.presentingViewController?.children,
            topVC.navigationController?.viewControllers
        ]

        for list in searchLists {
            guard let list else { continue }

            let scanList = isReversed ? list.reversed() : list

            for container in scanList {
                if container.isKind(of: targetVCType),
                   let nav = container.navigationController {
                    return (container, nav)
                }

                if let nav = container as? UINavigationController {
                    let navStack = isReversed ? nav.viewControllers.reversed() : nav.viewControllers
                    if let match = navStack.first(where: { $0.isKind(of: targetVCType) }) {
                        return (match, nav)
                    }
                }
            }
        }

        return nil
    }

    public func navigateToBackViewController(
        _ viewController: UIViewController.Type,
        isReversed: Bool = true
    ) {
        guard let result = findTargetViewController(viewController, isReversed: isReversed) else { return }

        let targetVC = result.vc
        let nav = result.nav

        guard let targetIndex = nav.viewControllers.firstIndex(where: { $0 === targetVC }) else { return }

        while nav.viewControllers.count - 1 > targetIndex {
            nav.popViewController(animated: false)
        }

        if nav.topViewController !== targetVC {
            nav.popViewController(animated: true)
        }
    }

}

extension UIImage {
    func resized(to targetSize: CGSize) -> UIImage {
        let scale = UIScreen.main.scale
        UIGraphicsBeginImageContextWithOptions(targetSize, false, scale)
        self.draw(in: CGRect(origin: .zero, size: targetSize))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return resizedImage ?? self
    }
}
