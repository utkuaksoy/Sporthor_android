//
//  CreatePostViewController.swift
//  Sporthor
//
//  Created by derTurke on 21.04.2025.
//
//

import UIKit
import ComponentKit
import Photos

final class CreatePostViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CreatePostPresenterProtocol {
        get { return self.basePresenter as! CreatePostPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var gradientView: CKGradientView = {
        let gradientView = CKGradientView(
            colors: [DesignKitColorName.backgroundPrimaryGreen.color.withAlphaComponent(0.2),
                     .clear],
            startPoint: CGPoint(x: 0.0, y: 0.0),
            endPoint: CGPoint(x: 0.5, y: 0.7)
        )
        gradientView.translatesAutoresizingMaskIntoConstraints = false
        return gradientView
    }()
    
    private lazy var displayImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedDisplayImageView(_:))))
        imageView.isUserInteractionEnabled = true
        return imageView
    }()
    
    private lazy var playerLayer: AVPlayerLayer = {
        let playerLayer = AVPlayerLayer()
        return playerLayer
    }()
    
    private lazy var queuePlayer: AVQueuePlayer = {
        let queuePlayer = AVQueuePlayer()
        return queuePlayer
    }()
    
    private var playerLooper: AVPlayerLooper?
    
    private lazy var headerLabel: CKLabel = {
        let label = CKLabel(text: "Galeri", textColor: .white, font: .bold03Compact)
        return label
    }()
    
    private lazy var multipleSelectButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.multipleSelectedWhite.image,
                              tag: 1)
        button.widthAnchor.constraint(equalToConstant: 20).isActive = true
        button.heightAnchor.constraint(equalToConstant: 20).isActive = true
        return button
    }()
    
    private lazy var cameraButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.cameraWhite.image,
                              tag: 2)
        button.widthAnchor.constraint(equalToConstant: 20).isActive = true
        button.heightAnchor.constraint(equalToConstant: 20).isActive = true
        return button
    }()
    
    private lazy var buttonStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 16)
        stackView.addArrangedSubviews([multipleSelectButton, cameraButton])
        return stackView
    }()
    
    private lazy var headerStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, distribution: .equalSpacing)
        stackView.addArrangedSubviews([headerLabel, buttonStackView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .clear
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
    
    // MARK: - Custom Methods
    @objc private func didTappedDisplayImageView(_ sender: UIImageView) {
        guard let player = playerLayer.player else { return }
        
        if player.timeControlStatus == .playing {
            player.pause()
        } else {
            player.play()
        }
    }
}

// MARK: - CreatePostPresenterDelegate
extension CreatePostViewController: CreatePostPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = color
    }
    
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isCloseWhiteExist = true
            navCon.isTextRightBarButtonItem = (
                "İleri",
                DesignKitColorName.backgroundPrimaryGreen.color,
                .bold03Compact
            )
            
            let titleAttributes: [NSAttributedString.Key: Any] = [
                .foregroundColor: UIColor.white,
                .font: UIFont.bold03Compact
            ]
            navCon.navigationBar.titleTextAttributes = titleAttributes
        }
    }
    
    func prepareNavigationDelegate() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    func prepareUI() {
        view.addSubview(gradientView)
        view.addSubview(displayImageView)
        view.addSubview(headerStackView)
        view.addSubview(collectionView)
        
        NSLayoutConstraint.activate([
            gradientView.topAnchor.constraint(equalTo: view.topAnchor),
            gradientView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            gradientView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            gradientView.heightAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.65),
            
            displayImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 12),
            displayImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            displayImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            displayImageView.heightAnchor.constraint(equalTo: view.widthAnchor),
            
            headerStackView.topAnchor.constraint(equalTo: displayImageView.bottomAnchor, constant: 32),
            headerStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            headerStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            collectionView.topAnchor.constraint(equalTo: headerStackView.bottomAnchor, constant: 16),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func changeMulitpleSelectedButtonImage(_ image: UIImage) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.multipleSelectButton.setImage(image)
        }
    }
    
    func previewImage(_ image: UIImage) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.playerLayer.player?.pause()
            self.playerLayer.removeFromSuperlayer()
            self.displayImageView.image = image
        }
    }
    
    func previewVideo(with avAsset: AVAsset) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            
            self.displayImageView.image = nil
            
            let playerItem = AVPlayerItem(asset: avAsset)
            let queuePlayer = AVQueuePlayer()
            self.queuePlayer = queuePlayer
            
            self.playerLooper = AVPlayerLooper(player: queuePlayer, templateItem: playerItem)
            
            self.playerLayer.player = queuePlayer
            self.playerLayer.frame = self.displayImageView.bounds
            
            if self.playerLayer.superlayer == nil {
                self.displayImageView.layer.addSublayer(self.playerLayer)
            }
            
            queuePlayer.play()
        }
    }
    
    func pauseVideo() {
        self.playerLayer.player?.pause()
    }
}

extension CreatePostViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return presenter.assetArray.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = SelectableImageViewCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        cell.bind(presenter.assetArray[indexPath.item],
                  multipleSelected: presenter.multipleSelected)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        presenter.didSelectItem(at: indexPath)
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension CreatePostViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (collectionView.frame.size.width - 6) / 3,
                      height: (collectionView.frame.size.width - 6) / 3)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return .zero
    }
}
// MARK: - CKButtonDelegate
extension CreatePostViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

// MARK: - CustomNavigationControllerDelegate
extension CreatePostViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .close:
            presenter.didTappedCloseButton()
        case .textRight:
            self.playerLayer.player?.pause()
            presenter.didTappedNextButton()
        default:
            break
        }
    }
}
