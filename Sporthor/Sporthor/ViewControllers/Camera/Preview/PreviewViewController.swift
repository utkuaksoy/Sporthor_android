import UIKit
import AVFoundation
import Photos
import ComponentKit
import CommonKit


final class PreviewViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: PreviewPresenterProtocol {
        get { return self.basePresenter as! PreviewPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.setCornerRadius(8)
        imageView.layer.masksToBounds = true
        imageView.isUserInteractionEnabled = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var videoPlayerView: UIView = {
        let view = UIView()
        view.clipsToBounds = true
        view.setCornerRadius(8)
        view.backgroundColor = .clear
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var player: AVPlayer = {
        let player = AVPlayer()
        return player
    }()
    
    private var playerLayer: AVPlayerLayer?
    
    private lazy var libraryButton: CKButton = {
        let button = CKButton(delegate: self,
                              cornerRadius: 4,
                              borderWidth: 2,
                              borderColor: .white,
                              tag: 0)
//        button.heightAnchor.constraint(equalToConstant: 40).isActive = true
        button.widthAnchor.constraint(equalToConstant: 40).isActive = true
        return button
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 20,
                              image: Asset.arrowRight.image,
                              tag: 1)
//        button.heightAnchor.constraint(equalToConstant: 40).isActive = true
        button.widthAnchor.constraint(equalToConstant: 40).isActive = true
        return button
    }()
    
    private lazy var buttonStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, distribution: .equalSpacing)
        stackView.addArrangedSubviews([libraryButton, continueButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var storyContinueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Paylaş",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var textView: CKCustomTextView = {
        let textView = CKCustomTextView(customDelegate: self,
                                        textColor: DesignKitColorName.contentStrong900.color,
                                        font: .body04Compact,
                                        backgroundColor: .white,
                                        cornerRadius: 8,
                                        padding: 16,
                                        minHeight: 20,
                                        maxHeight: 160)
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.isHidden = true
        return textView
    }()
    
    private lazy var textLabel: CKLabel = {
        let label = CKLabel(delegate: self,
                            textColor: DesignKitColorName.contentStrong900.color,
                            backgroundColor: .white,
                            cornerRadius: 8,
                            numberOfLines: 0,
                            font: .body04Compact,
                            tag: 1,
                            padding: UIEdgeInsets(top: 16, left: 16, bottom: 16, right: 16))
        addGestureRecognizer(label)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.isHidden = true
        return label
    }()
    
    private lazy var locationLabel: CKLabel = {
        let label = CKLabel(delegate: self,
                            textColor: DesignKitColorName.contentStrong900.color,
                            backgroundColor: .white,
                            cornerRadius: 8,
                            numberOfLines: 0,
                            font: .heading05,
                            tag: 2,
                            padding: UIEdgeInsets(top: 16, left: 16, bottom: 16, right: 16))
        addGestureRecognizer(label)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.isHidden = true
        return label
    }()
    
    private lazy var trashZoneView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.clipsToBounds = true
        view.setCornerRadius(32)
        view.alpha = 0
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var trashZoneImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.redTrashIcon.image)
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()

    // MARK: - Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if let navCon = navigationController as? CustomNavigationController {
            navCon.navigationItem.hidesBackButton = true
        }
        player.pause()
    }

    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        playerLayer?.frame = videoPlayerView.bounds
    }
    
    // MARK: - Custom Methods
    private func loadVideo() {
        guard let video = presenter.video else { return }
        let playerItem = AVPlayerItem(url: video)
        player = AVPlayer(playerItem: playerItem)
        player.isMuted = false

        playerLayer = AVPlayerLayer(player: player)
        playerLayer?.videoGravity = .resizeAspect
        playerLayer?.zPosition = -1
        textView.layer.zPosition = 1
        textLabel.layer.zPosition = 1
        locationLabel.layer.zPosition = 1
        videoPlayerView.layer.addSublayer(playerLayer!)
        player.play()
    }
    
    private func addGestureRecognizer(_ view: UIView) {
        view.isUserInteractionEnabled = true
        let panGesture = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
        panGesture.delegate = self
        view.addGestureRecognizer(panGesture)
        let rotateGesture = UIRotationGestureRecognizer(target: self, action: #selector(handleRotation(_:)))
        rotateGesture.delegate = self
        view.addGestureRecognizer(rotateGesture)
        let pinchGesture = UIPinchGestureRecognizer(target: self, action: #selector(handlePinch(_:)))
        pinchGesture.delegate = self
        view.addGestureRecognizer(pinchGesture)
    }
    
    @objc private func handlePan(_ gesture: UIPanGestureRecognizer) {
        guard let targetView = gesture.view else { return }
        let translation = gesture.translation(in: view)
        targetView.center = CGPoint(x: targetView.center.x + translation.x, y: targetView.center.y + translation.y)
        gesture.setTranslation(.zero, in: view)

        let isOverTrash = trashZoneView.frame.contains(gesture.location(in: view))
        
        switch gesture.state {
        case .began:
            UIView.animate(withDuration: 0.2) {
                self.trashZoneView.alpha = 1.0
            }
        case .changed:
            trashZoneView.tintColor = isOverTrash ? .red : .white.withAlphaComponent(0.5)
        case .ended:
            if isOverTrash {
                textView.text = nil
                targetView.isHidden = true
                switch targetView.tag {
                case 1:
                    textLabel.text = nil
                case 2:
                    locationLabel.text = nil
                default:
                    break
                }
            }
            UIView.animate(withDuration: 0.2) {
                self.trashZoneView.alpha = 0.0
            }
        default:
            break
        }
    }

    
    @objc private func handleRotation(_ gesture: UIRotationGestureRecognizer) {
        guard let view = gesture.view else { return }
        view.transform = view.transform.rotated(by: gesture.rotation)
        gesture.rotation = 0
    }
    
    @objc private func handlePinch(_ gesture: UIPinchGestureRecognizer) {
        guard let label = gesture.view as? CKLabel else { return }
        let currentFontSize = label.font.pointSize
        let scale = gesture.scale
        var newFontSize = currentFontSize * scale

        newFontSize = max(10, min(newFontSize, 60))
        label.font = label.font.withSize(newFontSize)

        label.invalidateIntrinsicContentSize()
        gesture.scale = 1.0
    }
}

// MARK: - PreviewPresenterDelegate
extension PreviewViewController: PreviewPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = color
    }
    
    func prepareNavigationBar() {
        if let nav = navigationController as? CustomNavigationController {
            nav.isCloseBackgroundBlackExist = true
            if presenter.feedType == .story && presenter.video == nil {
                nav.isLocationExist = true
                nav.isWriteLabelExist = true
            }
            nav.navigationBar.titleTextAttributes = [
                .foregroundColor: UIColor.white,
                .font: UIFont.bold03Compact
            ]
        }
    }
    
    func prepareNavigationBarDelegate() {
        if let nav = navigationController as? CustomNavigationController {
            nav.customDelegate = self
            nav.navigationItem.hidesBackButton = true
        }
    }
    
    func prepareImageUI() {
        imageView.image = presenter.image
        view.addSubview(imageView)
        if presenter.feedType == .post {
            view.addSubview(buttonStackView)
        } else {
            view.addSubview(storyContinueButton)
            imageView.addSubview(textView)
            imageView.addSubview(textLabel)
            imageView.addSubview(locationLabel)
            
            trashZoneView.addSubview(trashZoneImageView)
            imageView.addSubview(trashZoneView)
        }
        
        NSLayoutConstraint.activate([
            imageView.topAnchor.constraint(
                equalTo: presenter.feedType == .post ? view.safeAreaLayoutGuide.topAnchor : view.topAnchor,
                constant: presenter.feedType == .post ? 24 : 0
            ),
            imageView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            imageView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            imageView.bottomAnchor.constraint(equalTo: presenter.feedType == .post ? buttonStackView.topAnchor : storyContinueButton.topAnchor, constant: -12),
        ])
        
        if presenter.feedType == .story {
            NSLayoutConstraint.activate([
                textView.centerYAnchor.constraint(equalTo: imageView.centerYAnchor),
                textView.leadingAnchor.constraint(equalTo: imageView.leadingAnchor, constant: 16),
                textView.trailingAnchor.constraint(equalTo: imageView.trailingAnchor, constant: -16),
                
                textLabel.centerYAnchor.constraint(equalTo: imageView.centerYAnchor),
                textLabel.centerXAnchor.constraint(equalTo: imageView.centerXAnchor),
                textLabel.widthAnchor.constraint(lessThanOrEqualTo: imageView.widthAnchor, constant: -32),
                
                locationLabel.centerYAnchor.constraint(equalTo: imageView.centerYAnchor),
                locationLabel.centerXAnchor.constraint(equalTo: imageView.centerXAnchor),
                locationLabel.widthAnchor.constraint(lessThanOrEqualTo: imageView.widthAnchor, constant: -32),
                
                trashZoneView.centerXAnchor.constraint(equalTo: imageView.centerXAnchor),
                trashZoneView.bottomAnchor.constraint(equalTo: imageView.safeAreaLayoutGuide.bottomAnchor,
                                                      constant: -32),
                trashZoneView.widthAnchor.constraint(equalToConstant: 64),
                trashZoneView.heightAnchor.constraint(equalToConstant: 64),
                trashZoneImageView.centerXAnchor.constraint(equalTo: trashZoneView.centerXAnchor),
                trashZoneImageView.centerYAnchor.constraint(equalTo: trashZoneView.centerYAnchor),
                trashZoneImageView.widthAnchor.constraint(equalToConstant: 32),
                trashZoneImageView.heightAnchor.constraint(equalToConstant: 32),
            ])
        }
        
        presenter.feedType == .post ? prepareButtonStackView() : prepareStoryButton()
    }
    
    func prepareVideoUI() {
        view.addSubview(videoPlayerView)
        if presenter.feedType == .post {
            view.addSubview(buttonStackView)
        } else {
            view.addSubview(storyContinueButton)
            videoPlayerView.addSubview(textView)
            videoPlayerView.addSubview(textLabel)
            videoPlayerView.addSubview(locationLabel)
        }
        
        NSLayoutConstraint.activate([
            videoPlayerView.topAnchor.constraint(
                equalTo: presenter.feedType == .post ? view.safeAreaLayoutGuide.topAnchor : view.topAnchor,
                constant: presenter.feedType == .post ? 24 : 0
            ),
            videoPlayerView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            videoPlayerView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            videoPlayerView.bottomAnchor.constraint(equalTo: presenter.feedType == .post ? buttonStackView.topAnchor : storyContinueButton.topAnchor, constant: -12),
        ])
        
        if presenter.feedType == .story {
            NSLayoutConstraint.activate([
                textView.centerYAnchor.constraint(equalTo: videoPlayerView.centerYAnchor),
                textView.leadingAnchor.constraint(equalTo: videoPlayerView.leadingAnchor, constant: 16),
                textView.trailingAnchor.constraint(equalTo: videoPlayerView.trailingAnchor, constant: -16),
                
                textLabel.centerYAnchor.constraint(equalTo: videoPlayerView.centerYAnchor),
                textLabel.centerXAnchor.constraint(equalTo: videoPlayerView.centerXAnchor),
                textLabel.widthAnchor.constraint(lessThanOrEqualTo: videoPlayerView.widthAnchor, constant: -32),
                
                locationLabel.centerYAnchor.constraint(equalTo: videoPlayerView.centerYAnchor),
                locationLabel.centerXAnchor.constraint(equalTo: videoPlayerView.centerXAnchor),
                locationLabel.widthAnchor.constraint(equalTo: videoPlayerView.widthAnchor, constant: -32),
            ])
        }
        
        presenter.feedType == .post ? prepareButtonStackView() : prepareStoryButton()
        loadVideo()
    }
    
    private func prepareButtonStackView() {
        NSLayoutConstraint.activate([
            buttonStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
            buttonStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 32),
            buttonStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -32),
            buttonStackView.heightAnchor.constraint(equalToConstant: 40)
        ])
    }
    
    private func prepareStoryButton() {
        NSLayoutConstraint.activate([
            storyContinueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
            storyContinueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            storyContinueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            storyContinueButton.heightAnchor.constraint(equalToConstant: 46)
        ])
    }
    
    func prepareLibraryButtonImage(_ image: UIImage) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            libraryButton.setImage(image)
        }
    }
    
    func changeHiddenStoryContinueButton(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            storyContinueButton.isHidden = isHidden
        }
    }
    
    func changeNavigatonBarItems(_ isTappedWriteLabel: Bool) {
        if let navCon = navigationController as? CustomNavigationController {
            navigationItem.hidesBackButton = true
            navigationItem.leftBarButtonItem = nil
            navigationItem.leftBarButtonItems = []
            navigationItem.rightBarButtonItems = nil
            navigationItem.rightBarButtonItems = []
            navCon.isCloseBackgroundBlackExist = !isTappedWriteLabel
            navCon.isLocationExist = !isTappedWriteLabel
            navCon.isWriteLabelExist = !isTappedWriteLabel
            navCon.isTextRightBarButtonItem = (isTappedWriteLabel ? "Bitti" : "", .white, .bold03Compact)
        }
    }
    
    func changeTextViewHidden(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            UIView.transition(with: self.textLabel, duration: 0.3, options: .transitionCrossDissolve, animations: {
                self.textView.isHidden = isHidden
            })
        }
    }
    
    func changeTextLabelHidden(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            UIView.transition(with: self.textLabel, duration: 0.3, options: .transitionCrossDissolve, animations: {
                self.textLabel.isHidden = isHidden
            })
        }
    }
    
    func focusedTextView() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.textView.becomeFirstResponder()
        }
    }
    
    func changeTextLabelText(_ text: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.textLabel.text = text
        }
    }
    
    func changeTextViewText(_ text: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.textView.text = text
        }
    }
    
    func unfocusedTextView() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.textView.resignFirstResponder()
        }
    }
    
    func controlImageText() {
        if let originalImage = imageView.image {
            let newImage = BaseHelper.shared.imageWithTextFromLabels(image: originalImage, labels: [textLabel, locationLabel], imageView: imageView)
            presenter.uploadImage(image: newImage)
        }
    }
    
    func controlVideoText(_ video: URL) {
        // TODO: İncelenecek
    }
    
    func didSelectLocation(name: String) {
        locationLabel.text = "📍" + name
        locationLabel.isHidden = false
        locationLabel.invalidateIntrinsicContentSize()
    }
}

extension PreviewViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

extension PreviewViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .close:
            presenter.didTappedBackButton()
        case .writeLabel:
            presenter.didTappedWriteLabel()
        case .textRight:
            presenter.didTappedFinish()
        case .location:
            presenter.openLocations()
        default:
            break
        }
    }
}

extension PreviewViewController: CKCustomTextViewDelegate {
    func textViewDidChange(_ ckCustomTextView: CKCustomTextView) {
        UIView.performWithoutAnimation {
            ckCustomTextView.invalidateIntrinsicContentSize()
        }
    }
    
    func textViewDidEndEditing(_ ckCustomTextView: CKCustomTextView) {
        guard let text = ckCustomTextView.text else { return }
        presenter.textViewDidEndEditing(text)
    }
}

extension PreviewViewController: CKLabelDelegate {
    func didTapCKLabel(tag: Int) {
        switch tag {
        case 1:
            presenter.didTappedTextLabel(textLabel.text ?? "")
        default:
            break
        }
    }
}

extension PreviewViewController: UIGestureRecognizerDelegate {
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer,
                           shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
}
