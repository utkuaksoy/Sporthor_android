//
//  PostTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 26.06.2025.
//

import UIKit
import ComponentKit

enum MediaType: Int {
    case image = 0
    case video
}

protocol PostTableViewCellDelegate: AnyObject {
    func didTappedUsernameInHeaderView(_ username: String, userId: String)
    func didTappedTripleButtonInHeaderView(_ model: Post)
    func didTappedUsernameInLikeView(_ username: String, userId: String)
    func didTappedUsernameInCommentView(_ username: String, userId: String)
    func didTappedActionButton(tag: Int, indexPath: IndexPath)
    func didZoomingImagePost(started: Bool)
}

final class PostTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var ckPostHeaderView: CKPostHeaderView = {
        let postHeader = CKPostHeaderView()
        postHeader.translatesAutoresizingMaskIntoConstraints = false
        return postHeader
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumLineSpacing = 0
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.isPagingEnabled = true
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    private lazy var pageControl: CKWormPageControl = {
        let pageControl = CKWormPageControl(tintColor: .black.withAlphaComponent(0.4),
                                            currentPageTintColor: .white,
                                            dotSize: 8,
                                            wormSize: 20,
                                            dotSpacing: 8)
        pageControl.translatesAutoresizingMaskIntoConstraints = false
        return pageControl
    }()
    
    private lazy var ckPostActionView: CKPostActionView = {
        let ckPostActionView = CKPostActionView()
        ckPostActionView.translatesAutoresizingMaskIntoConstraints = false
        return ckPostActionView
    }()
    
    private lazy var ckPostLikeView: CKPostLikeView = {
        let ckPostActionView = CKPostLikeView()
        ckPostActionView.translatesAutoresizingMaskIntoConstraints = false
        return ckPostActionView
    }()
    
    private lazy var ckPostCommentView: CKPostCommentView = {
        let ckPostCommentView = CKPostCommentView()
        ckPostCommentView.translatesAutoresizingMaskIntoConstraints = false
        return ckPostCommentView
    }()
    
    private lazy var timeLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color, font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Members
    private weak var delegate: PostTableViewCellDelegate?
    private var post: Post?
    private var postMedias: [MediaItem] = []
    private var indexPath: IndexPath?
    private var collectionViewHeightConstraint: NSLayoutConstraint!
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(ckPostHeaderView)
        contentView.addSubview(collectionView)
        contentView.addSubview(pageControl)
        contentView.addSubview(ckPostActionView)
        contentView.addSubview(ckPostLikeView)
        contentView.addSubview(ckPostCommentView)
        contentView.addSubview(timeLabel)
        
        NSLayoutConstraint.activate([
            ckPostHeaderView.topAnchor.constraint(equalTo: contentView.topAnchor),
            ckPostHeaderView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            ckPostHeaderView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            ckPostHeaderView.heightAnchor.constraint(equalToConstant: 40),
            
            collectionView.topAnchor.constraint(equalTo: ckPostHeaderView.bottomAnchor, constant: 8),
            collectionView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            collectionView.heightAnchor.constraint(equalTo: contentView.widthAnchor),
            
            pageControl.bottomAnchor.constraint(equalTo: collectionView.bottomAnchor, constant: -15),
            pageControl.centerXAnchor.constraint(equalTo: collectionView.centerXAnchor),
            pageControl.heightAnchor.constraint(equalToConstant: 8),
            
            ckPostActionView.topAnchor.constraint(equalTo: collectionView.bottomAnchor, constant: 16),
            ckPostActionView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            ckPostActionView.heightAnchor.constraint(equalToConstant: 24),
            
            ckPostLikeView.topAnchor.constraint(equalTo: ckPostActionView.bottomAnchor, constant: 16),
            ckPostLikeView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            ckPostLikeView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            ckPostCommentView.topAnchor.constraint(equalTo: ckPostLikeView.bottomAnchor, constant: 8),
            ckPostCommentView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            ckPostCommentView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            timeLabel.topAnchor.constraint(equalTo: ckPostCommentView.bottomAnchor, constant: 8),
            timeLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            timeLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            timeLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -32)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(_ model: Post,
              delegate: PostTableViewCellDelegate? = nil,
              indexPath: IndexPath) {
        self.delegate = delegate
        self.indexPath = indexPath
        
        // MARK: - Header
        ckPostHeaderView.bind(with: model.profileImageUrl,
                              placeholderImage: Asset.errorUserImage.image,
                              username: model.username,
                              userId: model.userId,
                              imageCornerRadius: 20,
                              tripleDotButtonImage: Asset.moreHoriz.image,
                              delegate: self)
        
        // MARK: - Preview
        post = model
        postMedias = model.media
        pageControl.setNumberOfPages(model.media.count)
        pageControl.isHidden = model.media.count <= 1
        
        // MARK: - Actions
        ckPostActionView.bind(
            delegate: self,
            actions: [
                (image: model.isLiked ? Asset.heart.image : Asset.like.image, count: model.likeCount, tag: 0),
                (image: Asset.comment.image, count: model.commentCount, tag: 1)
            ]
        )
        
        // MARK: - Likes
        ckPostLikeView.bind(
            delegate: self,
            likes: model.lastLikedUsers.suffix(3).map({ $0.profileImageUrl}),
            placeholderImage: Asset.errorUserImage.image,
            username: model.lastLikedUsers.last?.username ?? "",
            userId: model.lastLikedUsers.last?.userId ?? ""
        )
        
        // MARK: - Description And Comments
        var comments: [(username: String, comment: String, userId: String)] = []
        if !model.description.isEmpty {
            comments.append((username: model.username, comment: model.description, model.userId))
        }
        
        if let firstComment = model.lastComments.first {
            comments.append((firstComment.username, firstComment.text, firstComment.userId))
        }
        ckPostCommentView.bind(delegate: self, comments: comments)
        
        timeLabel.text = model.time
        
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func updateLike(_ model: Post) {
        ckPostActionView.updateImageAndCount(forTag: 0,
                                             newImage: model.isLiked ? Asset.heart.image : Asset.like.image,
                                             newCount: model.likeCount)
        ckPostLikeView.update(
            likes: model.lastLikedUsers.suffix(3).map({ $0.profileImageUrl}),
            placeholderImage: Asset.errorUserImage.image,
            username: model.lastLikedUsers.last?.username ?? "",
            userId: model.lastLikedUsers.last?.userId ?? ""
        )
    }
    
    func handleVideoPlayback(shouldPlay: Bool) {
        collectionView.visibleCells.forEach { cell in
            if let videoCell = cell as? VideoCollectionViewCell {
                if shouldPlay {
                    videoCell.changeIsMuted(false)
                    videoCell.playVideo()
                } else {
                    videoCell.changeIsMuted(true)
                    videoCell.pauseVideo()
                }
            }
        }
    }
}

extension PostTableViewCell: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return postMedias.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let postMedia = postMedias[indexPath.item]
        switch postMedia.type {
        case MediaType.image.rawValue:
            let cell = ZoomableImageViewCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(postMedia.url, delegate: self)
            return cell
        case MediaType.video.rawValue:
            let cell = VideoCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(with: postMedia.url)
            return cell
        default:
            return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
        }
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let currentPage = Int(scrollView.contentOffset.x / scrollView.frame.width)
        pageControl.setCurrentPage(currentPage)
        handleVideoPlaybackBasedOnVisibility()
    }
    
    func handleVideoPlaybackBasedOnVisibility() {
        collectionView.visibleCells.forEach { cell in
            if let videoCell = cell as? VideoCollectionViewCell {
                videoCell.changeIsMuted(false)
                videoCell.playVideo()
            }
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if let videoCell = cell as? VideoCollectionViewCell {
            videoCell.changeIsMuted(true)
            videoCell.pauseVideo()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: contentView.frame.width, height: contentView.frame.width)
    }
}

// MARK: - CKPostHeaderViewDelegate
extension PostTableViewCell: CKPostHeaderViewDelegate {
    func didTappedUsernameInHeaderView(_ username: String, userId: String) {
        delegate?.didTappedUsernameInHeaderView(username, userId: userId)
    }
    
    func didTappedTripleButton() {
        guard let _ = indexPath,
              let post,
              let delegate else { return }
        delegate.didTappedTripleButtonInHeaderView(post)
    }
}

// MARK: - CKPostActionViewDelegate
extension PostTableViewCell: CKPostActionViewDelegate {
    func didTappedActionButton(tag: Int) {
        guard let indexPath else { return }
        delegate?.didTappedActionButton(tag: tag, indexPath: indexPath)
    }
}

// MARK: - CKPostLikeViewDelegate
extension PostTableViewCell: CKPostLikeViewDelegate {
    func didTappedUsernameInLikeView(_ username: String, userId: String) {
        delegate?.didTappedUsernameInLikeView(username, userId: userId)
    }
}

// MARK: - CKPostCommentViewDelegate
extension PostTableViewCell: CKPostCommentViewDelegate {
    func didTappedUsernameInCommentView(_ username: String, userId: String) {
        delegate?.didTappedUsernameInCommentView(username, userId: userId)
    }
}

// MARK: - ZoomableImageViewCollectionViewCellDelegate
extension PostTableViewCell: ZoomableImageViewCollectionViewCellDelegate {
    func zooming(started: Bool) {
        delegate?.didZoomingImagePost(started: started)
    }
}
