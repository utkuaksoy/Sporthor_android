//
//  ChatMediaAndDocumentViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import AutoLayout
import AVKit
import ChatKit
import ChatCoordinator
import DesignKit
import Factory
import UIKit
import QuickLook
import UniformTypeIdentifiers

final class ChatMediaAndDocumentViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ChatMediaAndDocumentPresenterProtocol!
    
    // MARK: - Private Properties
    @LazyInjected(\.chatCoordinator) private var chatCoordinator
    
    // MARK: - Private UI Components
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private lazy var segmentedControl: UISegmentedControl = {
        let items = ["Medya", "Belgeler"]
        let segmentedControl = UISegmentedControl(items: items)
        segmentedControl.selectedSegmentIndex = 0
        segmentedControl.addTarget(self, action: #selector(segmentedControlValueChanged(_:)), for: .valueChanged)
        return segmentedControl
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.minimumInteritemSpacing = 1
        layout.minimumLineSpacing = 1
        
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .clear
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(MediaCollectionViewCell.self, forCellWithReuseIdentifier: MediaCollectionViewCell.reuseIdentifier)
        collectionView.register(DocumentCollectionViewCell.self, forCellWithReuseIdentifier: DocumentCollectionViewCell.reuseIdentifier)
        collectionView.contentInset = .init(top: 16, left: .zero, bottom: 16, right: .zero)
        return collectionView
    }()
    
    private lazy var emptyView: EmptyMessageView = {
        let view = EmptyMessageView()
        view.isHidden = true
        return view
    }()
    
    private weak var videoPlayer: AVPlayerViewController?
    private var fileURL: URL?
    
    // MARK: - Lifecycle Methods
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
}

// MARK: - ChatMediaAndDocumentPresenterDelegate
extension ChatMediaAndDocumentViewController: ChatMediaAndDocumentPresenterDelegate {
    
    func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white

        appearance.titleTextAttributes = [
            .foregroundColor: ColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]

        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.titleView = segmentedControl
        navigationItem.leftBarButtonItem = backButton
    }
    
    func reloadData() {
        collectionView.reloadData()
    }
    
    func showEmptyView(for type: ChatMediaAndDocumentType) {
        emptyView.configure(with: type.emptyTitleText)
        emptyView.isHidden = false
    }
    
    func hideEmptyView() {
        emptyView.isHidden = true
    }
}

// MARK: - UICollectionViewDataSource
extension ChatMediaAndDocumentViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if presenter.selectedSegmentIndex == .zero {
            return presenter.mediaItems?.count ?? .zero
        } else {
            return presenter.documentItems?.count ?? .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if presenter.selectedSegmentIndex == 0 {
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: MediaCollectionViewCell.reuseIdentifier, for: indexPath) as? MediaCollectionViewCell,
                  let item = presenter.mediaItems?[safe: indexPath.item]
            else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
            cell.configure(delegate: self, with: item)
            return cell
        } else {
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: DocumentCollectionViewCell.reuseIdentifier, for: indexPath) as? DocumentCollectionViewCell,
                  let item = presenter.documentItems?[safe: indexPath.item]
            else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
            cell.configure(delegate: self, with: item)
            return cell
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension ChatMediaAndDocumentViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if presenter.selectedSegmentIndex == 0 {
            let width = (collectionView.bounds.width - 2) / 3
            return CGSize(width: width, height: width)
        } else {
            return CGSize(width: collectionView.bounds.width, height: 72)
        }
    }
}

// MARK: - Actions
private extension ChatMediaAndDocumentViewController {
    @objc
    func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
    @objc
    func segmentedControlValueChanged(_ sender: UISegmentedControl) {
        presenter.didSelectSegment(at: sender.selectedSegmentIndex)
    }
}

private extension ChatMediaAndDocumentViewController {
    func setupViews() {
        view.addSubview(containerView) {
            $0.pin(edges: [.leading, .trailing], to: view)
            $0.top == view.safeAreaLayoutGuide.topAnchor
            $0.bottom == view.safeAreaLayoutGuide.bottomAnchor
        }
        containerView.addSubview(collectionView) {
            $0.pin(to: containerView)
        }
        
        containerView.addSubview(emptyView) {
            $0.top == containerView.topAnchor + 8
            $0.pin(edges: [.leading, .trailing, .bottom], to: containerView)
        }
    }
}

extension ChatMediaAndDocumentViewController: DocumentCollectionViewCellDelegate {
    func didTapDocument(fileUrl: URL?, fileExtension: String?) {
        openDocumentPicker(fileURL: fileUrl, fileType: fileExtension)
    }
}

// MARK: Document Picker

private extension ChatMediaAndDocumentViewController {
    
    private func openDocumentPicker(fileURL: URL?, fileType: String?) {
        guard let fileURL, let fileType else { return }
        chatCoordinator?.startDocumentPreview(
            presenter: self,
            with: fileURL,
            fileType: fileType
        )
    }
    
    private func playVideo(from url: URL) {
        let player = AVPlayer(url: url)
        let playerViewController = AVPlayerViewController()
        playerViewController.player = player

        present(playerViewController, animated: true) {
            player.play()
        }
    }
}

extension ChatMediaAndDocumentViewController: MediaCollectionViewCellDelegate {
    func didTapMediaItem(image: UIImage?, videoUrl: URL?, type: ChatMessageType) {
        switch type {
        case .image:
            guard let image else { return }
            chatCoordinator?.startFullScreenImage(presenter: self, image: image)
        case .video:
            guard let videoUrl else { return }
            playVideo(from: videoUrl)
        default: break
        }
    }
}
