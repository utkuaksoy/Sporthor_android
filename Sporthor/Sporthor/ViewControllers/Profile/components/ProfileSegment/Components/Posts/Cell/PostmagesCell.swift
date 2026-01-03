import ComponentKit
import ComponentBaseKit
import DesignKit
import Factory
import UIKit
import AVFoundation
import ThumbnailProviderKit

public protocol PostImagesCellDelegate: AnyObject {
    func didTapPost(selectedPostId: String)
}

final class PostImagesCell: UICollectionViewCell, ReusableView, ComponentDisplayer, ComponentDisplayerViewModelConfigurable {

    // MARK: - Private UI Elements

    private let containerView = UIView()
    private let postImageView = UIImageView()
    private let trailingImageView = UIImageView()

    // MARK: - Dependencies

    @LazyInjected(\.thumbnailProvider) private var mediaThumbnailProvider

    // MARK: - Private Properties

    private weak var delegate: PostImagesCellDelegate?
    private var viewModel: ProfileSegmentComponentViewModel?
    private var thumbnailTaskID: UUID?
    private var postId: String?
    
    // MARK: - Lifecycle

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
        setupConstraints()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        postImageView.kf.cancelDownloadTask()
        thumbnailTaskID = nil
        postImageView.image = nil
        trailingImageView.image = nil
        trailingImageView.isHidden = true
        thumbnailTaskID = nil
    }

    // MARK: - Configure

    func configure(
        with viewModel: ProfileSegmentComponentViewModel,
        at indexPath: IndexPath,
        delegate: PostImagesCellDelegate?
    ) {
        self.viewModel = viewModel
        self.delegate = delegate
    }

    func configurePost(
        delegate: AnyObject?,
        with imageUrl: String,
        isMultipleImage: Bool = false,
        isVideo: Bool = false,
        postId: String?
    ) {
        self.delegate = delegate as? PostImagesCellDelegate
        self.postId = postId
        if isVideo {
            configureVideoThumbnail(from: imageUrl)
        } else {
            configureImage(from: imageUrl)
        }

        updateTrailingIcon(isVideo: isVideo, isMultipleImage: isMultipleImage)
    }
    
    private func configureVideoThumbnail(from imageUrl: String) {
        guard let url = URL(string: imageUrl) else { return }
        
        let currentTaskID = UUID()
        thumbnailTaskID = currentTaskID
        
        if let cachedImage = mediaThumbnailProvider.getThumbnailSyncIfCached(for: url) {
            postImageView.image = cachedImage
            return
        }

        mediaThumbnailProvider.getThumbnail(for: url) { [weak self] thumbnailImage in
            DispatchQueue.main.async {
                guard let self = self else { return }
                guard self.thumbnailTaskID == currentTaskID else { return }
                if let thumbnailImage = thumbnailImage {
                    self.postImageView.image = thumbnailImage
                }
            }
        }
    }

    private func configureImage(from imageUrl: String) {
        postImageView.setImage(with: imageUrl)
    }

    private func updateTrailingIcon(isVideo: Bool, isMultipleImage: Bool) {
        if isVideo {
            trailingImageView.image = .playCircle
            trailingImageView.isHidden = false
        } else if isMultipleImage {
            trailingImageView.image = .multipleImageIcon
            trailingImageView.isHidden = false
        } else {
            trailingImageView.isHidden = true
        }
    }
}

// MARK: - Private Helpers

private extension PostImagesCell {
    
    func setupUI() {
        containerView.translatesAutoresizingMaskIntoConstraints = false
        containerView.clipsToBounds = true
        contentView.addSubview(containerView)
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(containerTapped))
        containerView.isUserInteractionEnabled = true
        containerView.addGestureRecognizer(tapGesture)

        postImageView.translatesAutoresizingMaskIntoConstraints = false
        postImageView.contentMode = .scaleAspectFill
        postImageView.clipsToBounds = true
        postImageView.backgroundColor = DesignKitColorName.backgroundWeak100.color
        containerView.addSubview(postImageView)

        trailingImageView.translatesAutoresizingMaskIntoConstraints = false
        trailingImageView.contentMode = .scaleAspectFill
        trailingImageView.isHidden = true
        containerView.addSubview(trailingImageView)
    }

    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),

            postImageView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            postImageView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            postImageView.topAnchor.constraint(equalTo: containerView.topAnchor),
            postImageView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),

            trailingImageView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -8),
            trailingImageView.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 8),
            trailingImageView.widthAnchor.constraint(equalToConstant: 20),
            trailingImageView.heightAnchor.constraint(equalToConstant: 20),
        ])
    }
    
    @objc
    private func containerTapped() {
        guard let postId else { return }
        delegate?.didTapPost(selectedPostId: postId)
    }
}
