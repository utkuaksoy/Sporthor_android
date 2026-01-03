import UIKit
import ComponentKit
import AVFoundation

final class VideoCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements

    // Player container view, içine playerLayer ekleniyor, zoom & pan için
    private lazy var playerContainerView: UIView = {
        let view = UIView(frame: contentView.bounds)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.clipsToBounds = true
        return view
    }()
    
    private lazy var player: AVPlayer = {
        let player = AVPlayer()
        player.actionAtItemEnd = .none
        return player
    }()
    
    private lazy var playerLayer: AVPlayerLayer = {
        let layer = AVPlayerLayer(player: player)
        layer.videoGravity = .resizeAspect
        layer.frame = playerContainerView.bounds
        return layer
    }()
    
    private lazy var muteButton: CKButton = {
        let button = CKButton(delegate: self,
                              buttonBackgroundColor: .white,
                              cornerRadius: 12,
                              image: Asset.volumeHigh.image,
                              tag: 0)
        button.imageView?.contentMode = .center
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var replayButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Tekrar İzle",
                              titleColor: .white,
                              font: .bold03Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.isHidden = true
        return button
    }()
    
    private lazy var blurView: UIView = {
        let view = UIView()
        view.isHidden = true
        view.backgroundColor = .black.withAlphaComponent(0.5)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Properties
    
    private var isMuted: Bool = false
    private var playerItemContext = 0
    private var playerItem: AVPlayerItem?

    // Zoom & Gesture-related properties
    private var overlayView: UIView?
    private var windowPlayerContainerView: UIView?
    private var startingFrame = CGRect.zero
    private var fixedPinchCenter: CGPoint = .zero
    
    // Constants for overlay animation
    private let maxOverlayAlpha: CGFloat = 0.8
    private let minOverlayAlpha: CGFloat = 0.4
    private let animationDuration: TimeInterval = 0.3
    
    // MARK: - Lifecycle
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
        setupPlayerLayer()
        setupGestures()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
        setupPlayerLayer()
        setupGestures()
    }
    
    deinit {
        removeAllObservers()
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        resetPlayer()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        playerContainerView.frame = contentView.bounds
        playerLayer.frame = playerContainerView.bounds
    }
    
    // MARK: - Public Methods
    
    func bind(with urlString: String) {
        guard let url = URL(string: urlString) else { return }
        
        resetPlayer()
        
        playerItem = AVPlayerItem(url: url)
        player.replaceCurrentItem(with: playerItem)
        
        setupObservers()
    }
    
    func playVideo() {
        guard player.timeControlStatus != .playing else { return }
        
        let currentTime = player.currentTime().seconds
        let duration = player.currentItem?.duration.seconds ?? 0
        
        if duration.isFinite && currentTime >= (duration - 0.5) {
            player.seek(to: .zero)
        }
        
        player.play()
        player.isMuted = isMuted
    }
    
    func pauseVideo() {
        guard player.timeControlStatus == .playing else { return }
        player.pause()
    }
    
    func changeIsMuted(_ isMuted: Bool) {
        self.isMuted = isMuted
        player.isMuted = isMuted
    }
    
    // MARK: - Private Methods
    
    private func setupUI() {
        // playerContainerView ekleniyor, playerLayer bu view içinde
        contentView.addSubview(playerContainerView)
        playerContainerView.layer.addSublayer(playerLayer)
        
        contentView.addSubview(muteButton)
        contentView.addSubview(blurView)
        blurView.addSubview(replayButton)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleVideoTap))
        tapGesture.cancelsTouchesInView = false
        contentView.addGestureRecognizer(tapGesture)
        
        NSLayoutConstraint.activate([
            playerContainerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            playerContainerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            playerContainerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            playerContainerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            muteButton.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            muteButton.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            muteButton.widthAnchor.constraint(equalToConstant: 24),
            muteButton.heightAnchor.constraint(equalToConstant: 24),
            
            blurView.topAnchor.constraint(equalTo: contentView.topAnchor),
            blurView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            blurView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            blurView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            replayButton.centerXAnchor.constraint(equalTo: blurView.centerXAnchor),
            replayButton.centerYAnchor.constraint(equalTo: blurView.centerYAnchor),
        ])
    }
    
    private func setupPlayerLayer() {
        // Burada playerLayer zaten playerContainerView içinde
        // contentView.layer.insertSublayer(playerLayer, at: 0) kullanılmaz
        // Çünkü playerLayer playerContainerView.layer'a eklendi
    }
    
    private func setupObservers() {
        // PlayerItem status observer
        playerItem?.addObserver(self,
                                forKeyPath: #keyPath(AVPlayerItem.status),
                                options: [.old, .new],
                                context: &playerItemContext)
        
        // Video end notification
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(playerDidFinishPlaying),
                                               name: .AVPlayerItemDidPlayToEndTime,
                                               object: player.currentItem)
    }
    
    private func removeAllObservers() {
        NotificationCenter.default.removeObserver(self,
                                                  name: .AVPlayerItemDidPlayToEndTime,
                                                  object: nil)
        
        playerItem?.removeObserver(self, forKeyPath: #keyPath(AVPlayerItem.status))
    }
    
    private func resetPlayer() {
        pauseVideo()
        removeAllObservers()
        player.replaceCurrentItem(with: nil)
        playerItem = nil
        replayButton.isHidden = true
        blurView.isHidden = true
        muteButton.isHidden = false
        
        // Zoom ile ilgili cleanup
        windowPlayerContainerView?.removeFromSuperview()
        overlayView?.removeFromSuperview()
        playerContainerView.isHidden = false
    }
    
    // MARK: - KVO Handling
    
    override func observeValue(forKeyPath keyPath: String?,
                               of object: Any?,
                               change: [NSKeyValueChangeKey : Any]?,
                               context: UnsafeMutableRawPointer?) {
        
        guard context == &playerItemContext else {
            super.observeValue(forKeyPath: keyPath, of: object, change: change, context: context)
            return
        }
        
        if keyPath == #keyPath(AVPlayerItem.status) {
            let status: AVPlayerItem.Status
            
            if let statusNumber = change?[.newKey] as? NSNumber {
                status = AVPlayerItem.Status(rawValue: statusNumber.intValue)!
            } else {
                status = .unknown
            }
            
            switch status {
            case .readyToPlay:
                player.isMuted = isMuted
            case .failed:
                if let error = playerItem?.error {
                    print("Video yükleme hatası: \(error.localizedDescription)")
                }
            case .unknown:
                print("Video durumu bilinmiyor")
            @unknown default:
                print("Bilinmeyen video durumu")
            }
        }
    }
    
    @objc private func handleVideoTap() {
        if player.timeControlStatus == .playing {
            pauseVideo()
        } else {
            playVideo()
        }
    }
    
    // MARK: - Video End Handling
    
    @objc private func playerDidFinishPlaying() {
        muteButton.isHidden = true
        replayButton.isHidden = false
        blurView.isHidden = false
    }
    
    // MARK: - Zoom & Pan Gesture Setup
    
    private func setupGestures() {
        let pinch = UIPinchGestureRecognizer(target: self, action: #selector(handlePinch(_:)))
        pinch.delegate = self
        playerContainerView.addGestureRecognizer(pinch)
        
        let pan = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
        pan.delegate = self
        playerContainerView.addGestureRecognizer(pan)
    }
    
    @objc private func handlePinch(_ gesture: UIPinchGestureRecognizer) {
        guard let window = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) else { return }
        
        switch gesture.state {
        case .began:
            guard gesture.scale > 1 else { return }
            // Zoom başladı, overlay ve geçici view oluştur
            muteButton.isHidden = true
            blurView.isHidden = true
            
            let overlay = UIView(frame: window.bounds)
            overlay.backgroundColor = .black
            overlay.alpha = minOverlayAlpha
            window.addSubview(overlay)
            overlayView = overlay
            
            startingFrame = playerContainerView.convert(playerContainerView.bounds, to: window)
            
            guard let snapshot = playerContainerView.snapshotView(afterScreenUpdates: false) else { return }
            snapshot.frame = startingFrame
            window.addSubview(snapshot)
            windowPlayerContainerView = snapshot
            
            playerContainerView.isHidden = true
            fixedPinchCenter = gesture.location(in: window)
            
        case .changed:
            guard let zoomView = windowPlayerContainerView else { return }
            
            let currentScale = zoomView.frame.size.width / startingFrame.size.width
            var newScale = currentScale * gesture.scale
            newScale = max(1.0, min(newScale, 3.0))
            
            zoomView.transform = .identity
            
            let anchor = fixedPinchCenter
            let dx = anchor.x - zoomView.center.x
            let dy = anchor.y - zoomView.center.y
            
            var transform = CGAffineTransform.identity
            transform = transform.translatedBy(x: dx, y: dy)
            transform = transform.scaledBy(x: newScale, y: newScale)
            transform = transform.translatedBy(x: -dx, y: -dy)
            zoomView.transform = transform
            
            gesture.scale = 1.0
            
            overlayView?.alpha = min(minOverlayAlpha + (newScale - 1), maxOverlayAlpha)
            
        case .ended, .cancelled, .failed:
            animateZoomReset()
            
        default:
            break
        }
    }
    
    @objc private func handlePan(_ gesture: UIPanGestureRecognizer) {
        guard let zoomView = windowPlayerContainerView else { return }
        
        let translation = gesture.translation(in: zoomView.superview)
        
        switch gesture.state {
        case .changed:
            zoomView.center = CGPoint(
                x: zoomView.center.x + translation.x,
                y: zoomView.center.y + translation.y
            )
            gesture.setTranslation(.zero, in: zoomView.superview)
            
        default:
            break
        }
    }
    
    private func animateZoomReset() {
        guard let zoomView = windowPlayerContainerView else { return }
        
        UIView.animate(withDuration: animationDuration, animations: {
            zoomView.transform = .identity
            zoomView.frame = self.startingFrame
            self.overlayView?.alpha = 0
        }, completion: { _ in
            zoomView.removeFromSuperview()
            self.overlayView?.removeFromSuperview()
            self.playerContainerView.isHidden = false
            self.muteButton.isHidden = false
            self.blurView.isHidden = true
        })
    }
}

// MARK: - CKButtonDelegate

extension VideoCollectionViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 0: // Mute/Unmute
            isMuted.toggle()
            player.isMuted = isMuted
            muteButton.setImage(isMuted ? Asset.volumeSlash.image : Asset.volumeHigh.image)
            
        case 1: // Replay
            replayButton.isHidden = true
            blurView.isHidden = true
            muteButton.isHidden = false
            player.seek(to: .zero)
            player.play()
            
        default:
            break
        }
    }
}

// MARK: - UIGestureRecognizerDelegate

extension VideoCollectionViewCell: UIGestureRecognizerDelegate {
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer,
                           shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
}
