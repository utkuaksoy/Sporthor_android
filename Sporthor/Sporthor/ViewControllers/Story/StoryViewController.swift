//
//  StoryViewController.swift
//  Sporthor
//
//  Created by derTurke on 28.04.2025.
//
//

import UIKit
import ComponentKit
import AVKit

final class StoryViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: StoryPresenterProtocol {
        get { return self.basePresenter as! StoryPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var backgroundView: UIView = {
        let view = UIView()
        view.backgroundColor = .black
        view.setCornerRadius(8)
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var progressView: CKProgressView = {
        let progressView = CKProgressView()
        progressView.translatesAutoresizingMaskIntoConstraints = false
        return progressView
    }()
    
    private lazy var ckPostHeaderView: CKPostHeaderView = {
        let postHeader = CKPostHeaderView()
        postHeader.translatesAutoresizingMaskIntoConstraints = false
        return postHeader
    }()
    
    private lazy var closeButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.closeWhite.image)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var tapGestureMainView: UITapGestureRecognizer = {
        let gesture = UITapGestureRecognizer(target: self, action: #selector(handleTapMainView(_:)))
        return gesture
    }()
    
    private lazy var panGestureRecognizer: UIPanGestureRecognizer = {
        let gesture = UIPanGestureRecognizer(target: self, action: #selector(handlePanGesture(_:)))
        return gesture
    }()
    
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.setCornerRadius(8)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var player: AVPlayer = {
        let player = AVPlayer()
        player.isMuted = false
        return player
    }()
    
    private lazy var playerLayer: AVPlayerLayer = {
        let layer = AVPlayerLayer(player: player)
        layer.videoGravity = .resizeAspectFill
        return layer
    }()
    
    private lazy var settingButton: CKButton = {
        let button = CKButton(delegate: self,
                              buttonBackgroundColor: .white,
                              cornerRadius: 16,
                              image: Asset.moreHoriz.image,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var longPressGestureRecognizer: UILongPressGestureRecognizer = {
        let gesture = UILongPressGestureRecognizer(target: self, action: #selector(handleLongPress(_:)))
        gesture.minimumPressDuration = 0.2
        return gesture
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
    @objc private func handleTapMainView(_ gesture: UITapGestureRecognizer) {
        let touchPoint = gesture.location(in: self.view)
        if touchPoint.x < view.bounds.width / 2 {
            presenter.didTapLeft()
        } else {
            presenter.didTapRight()
        }
    }
    
    @objc private func handlePanGesture(_ gesture: UIPanGestureRecognizer) {
        let translation = gesture.translation(in: view)
        
        switch gesture.state {
        case .changed:
            if abs(translation.x) > abs(translation.y) {
                backgroundView.transform = CGAffineTransform(translationX: translation.x, y: 0)
            }
        case .ended, .cancelled:
            if abs(translation.x) > 100 {
                if translation.x > 0 {
                    presenter.didSwipeLeft()
                } else {
                    presenter.didSwipeRight()
                }
                resetSwipe()
            } else if abs(translation.y) > 100 {
                presenter.dismiss()
            } else {
                resetSwipe()
            }
        default:
            break
        }
    }

    private func resetSwipe() {
        UIView.animate(withDuration: 0.3) {
            self.backgroundView.transform = CGAffineTransform.identity
        }
    }
    private func playVideo(urlString: String) {
        guard let url = URL(string: urlString) else { return }
        
        player = AVPlayer(url: url)
        player.addObserver(self,
                           forKeyPath: "timeControlStatus",
                           options: [.old, .new],
                           context: nil)
        
        playerLayer = AVPlayerLayer(player: player)
        playerLayer.frame = backgroundView.bounds
        backgroundView.layer.insertSublayer(playerLayer, at: 0)
        player.play()
    }
    override func observeValue(forKeyPath keyPath: String?,
                               of object: Any?,
                               change: [NSKeyValueChangeKey : Any]?,
                               context: UnsafeMutableRawPointer?) {

        if keyPath == "timeControlStatus" {
            switch player.timeControlStatus {
            case .waitingToPlayAtSpecifiedRate:
                presenter.showIndicator()
            case .paused:
                print("⏸ Video durdu")
            case .playing:
                presenter.hideIndicator()
                presenter.videoReadyToPlay()
            @unknown default:
                break
            }
        }
    }
    
    
    @objc private func handleLongPress(_ gesture: UILongPressGestureRecognizer) {
        switch gesture.state {
        case .began:
            pauseAnimation()
        case .ended, .cancelled, .failed:
            resumeAnimation()
        default:
            break
        }
    }
}

// MARK: - StoryPresenterDelegate
extension StoryViewController: StoryPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = color
    }
    
    func prepareUI() {
        view.addSubview(backgroundView)
        view.isUserInteractionEnabled = true
        view.addGestureRecognizer(tapGestureMainView)
        view.addGestureRecognizer(panGestureRecognizer)
        view.addGestureRecognizer(longPressGestureRecognizer)
        backgroundView.addSubview(progressView)
        backgroundView.addSubview(ckPostHeaderView)
        backgroundView.addSubview(closeButton)
        backgroundView.addSubview(settingButton)
        
        NSLayoutConstraint.activate([
            backgroundView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            backgroundView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            backgroundView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            backgroundView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
            
            progressView.topAnchor.constraint(equalTo: backgroundView.topAnchor, constant: 8),
            progressView.leadingAnchor.constraint(equalTo: backgroundView.leadingAnchor, constant: 16),
            progressView.trailingAnchor.constraint(equalTo: backgroundView.trailingAnchor, constant: -16),
            
            ckPostHeaderView.topAnchor.constraint(equalTo: progressView.bottomAnchor, constant: 16),
            ckPostHeaderView.leadingAnchor.constraint(equalTo: backgroundView.leadingAnchor, constant: 16),
            ckPostHeaderView.heightAnchor.constraint(equalToConstant: 32),
            
            closeButton.topAnchor.constraint(equalTo: progressView.bottomAnchor, constant: 16),
            closeButton.trailingAnchor.constraint(equalTo: backgroundView.trailingAnchor, constant: -16),
            closeButton.heightAnchor.constraint(equalToConstant: 32),
            closeButton.widthAnchor.constraint(equalToConstant: 32),
            
            settingButton.topAnchor.constraint(equalTo: closeButton.topAnchor),
            settingButton.leadingAnchor.constraint(equalTo: ckPostHeaderView.trailingAnchor, constant: 16),
            settingButton.trailingAnchor.constraint(equalTo: closeButton.leadingAnchor, constant: -16),
            settingButton.heightAnchor.constraint(equalTo: closeButton.heightAnchor),
            settingButton.widthAnchor.constraint(equalTo: closeButton.widthAnchor)
            
        ])
    }
    
    func prepareProgressView(steps: Int, selectedStep: Int, duration: TimeInterval) {
        progressView.bindWithAnimation(delegate: self,
                                       steps: steps,
                                       selectedStep: selectedStep,
                                       spacing: 4,
                                       durationPerStep: duration)
    }
    
    func prepareHeaderView(image: String, username: String, userId: String) {
        ckPostHeaderView.bind(with: image,
                              placeholderImage: Asset.errorUserImage.image,
                              username: username,
                              usernameTextColor: .white,
                              userId: userId,
                              imageCornerRadius: 16)
    }
    
    func clearCurrentMedia() {
       imageView.removeFromSuperview()
       playerLayer.removeFromSuperlayer()
       player.pause()
   }
    
    func showStoryDetail(_ detail: StoryDetail) {
        clearCurrentMedia()

        guard let firstMedia = detail.media else { return }

        if firstMedia.type == MediaType.image.rawValue {
            imageView.isHidden = false
            backgroundView.addSubview(imageView)
            NSLayoutConstraint.activate([
                imageView.topAnchor.constraint(equalTo: ckPostHeaderView.bottomAnchor, constant: 16),
                imageView.leadingAnchor.constraint(equalTo: backgroundView.leadingAnchor),
                imageView.trailingAnchor.constraint(equalTo: backgroundView.trailingAnchor),
                imageView.bottomAnchor.constraint(equalTo: backgroundView.bottomAnchor)
            ])
            imageView.setImage(with: firstMedia.url)
        } else if firstMedia.type == MediaType.video.rawValue {
            imageView.isHidden = true
            playVideo(urlString: firstMedia.url)
        }
    }
    
    func animateStoryTransition(for direction: SwipeDirection, updates: @escaping () -> Void) {
        // 1. Take a snapshot of the current content
        guard let oldSnapshot = backgroundView.snapshotView(afterScreenUpdates: false) else {
            updates()
            return
        }
        oldSnapshot.frame = backgroundView.frame
        oldSnapshot.backgroundColor = .black
        view.addSubview(oldSnapshot)

        // 2. Update the backgroundView with new content (hidden for now)
        updates()
        backgroundView.layoutIfNeeded()
        backgroundView.isHidden = true

        // 3. Take a snapshot of the new content
        guard let newSnapshot = backgroundView.snapshotView(afterScreenUpdates: true) else {
            backgroundView.isHidden = false
            oldSnapshot.removeFromSuperview()
            return
        }
        newSnapshot.frame = backgroundView.frame
        newSnapshot.backgroundColor = .black
        view.addSubview(newSnapshot)

        // 4. Set up 3D cube rotation effect
        var perspective = CATransform3DIdentity
        perspective.m34 = -1.0 / 500.0
        let angle: CGFloat = .pi / 2
        let directionMultiplier: CGFloat = direction == .left ? 1 : -1

        oldSnapshot.layer.transform = CATransform3DIdentity
        newSnapshot.layer.transform = CATransform3DRotate(perspective, -directionMultiplier * angle, 0, 1, 0)
        newSnapshot.alpha = 1.0
        backgroundView.isHidden = true

        // 5. Animate the cube transition
        UIView.animate(withDuration: 0.5, delay: 0, options: [.curveEaseInOut], animations: {
            oldSnapshot.layer.transform = CATransform3DRotate(perspective, directionMultiplier * angle, 0, 1, 0)
            oldSnapshot.alpha = 0.5
            newSnapshot.layer.transform = CATransform3DIdentity
            newSnapshot.alpha = 1.0
        }, completion: { _ in
            self.backgroundView.isHidden = false
            oldSnapshot.removeFromSuperview()
            newSnapshot.removeFromSuperview()
            self.backgroundView.layer.transform = CATransform3DIdentity
            self.backgroundView.backgroundColor = .black
        })
    }
    
    func settingButtonHidden(_ isHidden: Bool) {
        settingButton.isHidden = isHidden
    }
    
    func pauseAnimation() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            player.pause()
            progressView.pause()
        }
    }
    
    func resumeAnimation() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            player.play()
            progressView.resume()
        }
    }
}

// MARK: - CKButtonDelegate
extension StoryViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.ckButtonDidTap(tag)
    }
}

// MARK: - CKProgressViewDelegate
extension StoryViewController: CKProgressViewDelegate {
    func selectedStepDidFinish(step: Int) {
        presenter.selectedStepDidFinish(step)
    }
    
    func didFinishProgress() {
        
    }
}
