import UIKit
import ComponentBaseKit
import ComponentKit
import DesignKit

final class GroupHeaderCell: UICollectionViewCell, ReusableView {

    // MARK: - Private UI Elements

    private lazy var groupImageView: UIImageView = {
        let iv = UIImageView()
        iv.layer.cornerRadius = 36
        iv.clipsToBounds = true
        iv.contentMode = .scaleAspectFill
        iv.translatesAutoresizingMaskIntoConstraints = false
        return iv
    }()
    
    private lazy var groupNameLabel: UILabel = {
        let label = UILabel()
        label.font = .heading06
        label.numberOfLines = 0
        label.textColor = ColorName.contentStrong900.color
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var groupDateLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.numberOfLines = 0
        label.textColor = ColorName.contentSoft600.color
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var editGroupButton: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("Grup adı ve simgesini değiştirin", for: .normal)
        button.titleLabel?.font = .body04Compact
        button.setTitleColor(ColorName.blue600.color, for: .normal)
        button.addTarget(self, action: #selector(editButtonTapped(_:)), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Properties
    var editButtonTapped: (() -> Void)?
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configuration

    func configure(imageUrl: String, name: String, groupUserUser: Int) {
        groupImageView.setImage(with: imageUrl)
        groupNameLabel.text = name
        groupDateLabel.text = "Grup - \(groupUserUser) üye"
    }
    
    // MARK: - Actions
    @objc private func editButtonTapped(_ sender: UIButton) {
        editButtonTapped?()
    }
}

// MARK: - Setup
private extension GroupHeaderCell {
    func setupViews() {
        contentView.backgroundColor = .white
        contentView.addSubview(groupImageView)
        contentView.addSubview(groupNameLabel)
        contentView.addSubview(groupDateLabel)
        contentView.addSubview(editGroupButton)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            groupImageView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor),
            groupImageView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 24),
            groupImageView.widthAnchor.constraint(equalToConstant: 72),
            groupImageView.heightAnchor.constraint(equalToConstant: 72),
            
            groupNameLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            groupNameLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            groupNameLabel.topAnchor.constraint(equalTo: groupImageView.bottomAnchor, constant: 16),
            
            groupDateLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            groupDateLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            groupDateLabel.topAnchor.constraint(equalTo: groupNameLabel.bottomAnchor, constant: 4),
            
            editGroupButton.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            editGroupButton.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            editGroupButton.topAnchor.constraint(equalTo: groupDateLabel.bottomAnchor, constant: 8),
            editGroupButton.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -24)
        ])
    }
}
