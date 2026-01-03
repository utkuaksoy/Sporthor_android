//
//  CreateGroupChatViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import BarVisibilityKit
import ComponentKit
import DesignKit
import UIKit
import PhotosUI

final class CreateGroupChatViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: CreateGroupChatPresenterProtocol {
        get { return self.basePresenter as! CreateGroupChatPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements
        
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private lazy var tableView: UITableView = {
        let tv = UITableView(frame: .zero, style: .grouped)
        tv.delegate = self
        tv.dataSource = self
        tv.separatorStyle = .none
        tv.backgroundColor = ColorName.backgroundWhite0.color
        tv.register(ChatUserCell.self, forCellReuseIdentifier: ChatUserCell.reuseIdentifier)
        tv.translatesAutoresizingMaskIntoConstraints = false
        tv.showsVerticalScrollIndicator = false
        tv.contentInset = .init(top: 16, left: .zero, bottom: 24, right: .zero)
        return tv
    }()
    
    private lazy var headerView: CreateGroupHeaderView = {
        let view = CreateGroupHeaderView(
            frame: CGRect(
                x: 0,
                y: 0,
                width: UIScreen.main.bounds.width,
                height: presenter.fromViewType == .newGroup ? 137 : 44
            ),
            isNewGroup: presenter.fromViewType == .newGroup
        )
        view.delegate = self
        view.groupNameView.delegate = self
        return view
    }()
    
    private lazy var createGroupButton: CKButton = {
        let button = CKButton(
            title: "Grubu Oluştur",
            titleColor: ColorName.contentStrong900.color,
            buttonBackgroundColor: ColorName.backgroundPrimaryGreen.color,
            cornerRadius: 24,
            font: .bold03Compact,
            isEnabled: false
        )
        button.addTarget(self, action: #selector(createGroupButtonTapped), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private var isTabbarHidden: Bool {
        presenter.fromViewType == .newGroup ? false : true
    }
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        setupConstraints()
        configureUI()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: isTabbarHidden))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBar()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: isTabbarHidden))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        configureTabBarVisibility(at: .willDisappear)
        configureNavigationBarVisibility(at: .willDisappear)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        configureTabBarVisibility(at: .didDisappear)
    }
    
    // MARK: - Custom Methods
    
    private func configureUI() {
        configureNavigationBar()
        tableView.tableHeaderView = headerView
    }
    
    private func configureNavigationBar() {
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
        navigationItem.title =  presenter.fromViewType == .newGroup ? "Yeni Grup Sohbeti" : "Kişi Ekle"
        navigationItem.leftBarButtonItem = backButton
    }
    
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
    
    @objc
    private func createGroupButtonTapped() {
        switch presenter.fromViewType {
        case .newGroup:
            if let groupName = headerView.groupNameView.getText(), !groupName.isEmpty {
                presenter.createGroupButtonTapped(with: groupName)
            } else {
                showError("Lütfen grup adını giriniz")
            }
        case .addMembers(let groupId):
            presenter.updateGroupButtonTapped(with: groupId)
        }
    }
}

// MARK: - CreateGroupChatPresenterDelegate
extension CreateGroupChatViewController: CreateGroupChatPresenterDelegate {
    
    func reloadData() {
        tableView.reloadData()
    }
    
    func updateCreateGroupButton(isEnabled: Bool) {
        createGroupButton.setEnabled(isEnabled)
    }
    
    func showError(_ message: String) {
        let alert = UIAlertController(
            title: "Hata",
            message: message,
            preferredStyle: .alert
        )
        alert.addAction(UIAlertAction(title: "Tamam", style: .default))
        present(alert, animated: true)
    }
    
    func configureHeaderView(type: CreateGroupChatViewType) {
        switch type {
        case .newGroup:
            navigationItem.title = "Yeni Grup Sohbeti"
        case .addMembers(_):
            createGroupButton.setTitle("Kaydet")
        }
    }
}

// MARK: - Setup

private extension CreateGroupChatViewController {
    func setupViews() {
        view.addSubview(tableView)
        view.addSubview(createGroupButton)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.bottomAnchor.constraint(equalTo: createGroupButton.isEnabled ? createGroupButton.topAnchor : view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
            
            createGroupButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            createGroupButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            createGroupButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
            createGroupButton.heightAnchor.constraint(equalToConstant: 48)
        ])
    }
}

// MARK: - UITableViewDataSource

extension CreateGroupChatViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return presenter.numberOfSections()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.numberOfRows(in: section)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return presenter.cellForRow(at: indexPath, in: tableView)
    }
}

// MARK: - UITableViewDelegate

extension CreateGroupChatViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return presenter.heightForRowAt(at: indexPath, in: tableView)
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return presenter.heightForHeaderInSection(tableView, heightForHeaderInSection: section)
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        guard let title = presenter.titleForHeaderInSection(tableView, heightForHeaderInSection: section), presenter.fromViewType == .newGroup else {
            return nil
        }
        
        let headerView = GroupSectionHeaderView()
        headerView.configure(title: title)
        return headerView
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRow(at: indexPath)
    }
}

// MARK: - CreateGroupHeaderViewDelegate

extension CreateGroupChatViewController: CreateGroupHeaderViewDelegate {
    func createGroupHeaderView(
        _ headerView: CreateGroupHeaderView,
        searchTextDidChange text: String
    ) {
        presenter.search(text: text)
    }
}

// MARK: - GroupNameViewDelegate
extension CreateGroupChatViewController: GroupNameViewDelegate {
    
    func presentImagePickerOptions() {
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let cameraAction = UIAlertAction(title: "Kamera", style: .default) { [weak self] _ in
            self?.headerView.groupNameView.handleCameraSelection()
        }
        
        let galleryAction = UIAlertAction(title: "Galeri", style: .default) { [weak self] _ in
            self?.headerView.groupNameView.handlePhotoLibrarySelection()
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true)
    }
    
    func presentCamera() {
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = .camera
        imagePicker.delegate = self
        present(imagePicker, animated: true)
    }
    
    func presentPhotoLibrary() {
        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = 1
        
        let picker = PHPickerViewController(configuration: config)
        picker.delegate = self
        present(picker, animated: true)
    }
    
    func showPermissionAlert(for type: PermissionType) {
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
        
        present(alertController, animated: true)
    }
}

// MARK: - UIImagePickerControllerDelegate, UINavigationControllerDelegate

extension CreateGroupChatViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true)
        if let image = info[.originalImage] as? UIImage {
            headerView.groupNameView.configureImageView(image: image)
            uploadImage(image)
        }
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true)
    }
    
    private func uploadImage(_ image: UIImage) {
        presenter.uploadImage(with: image)
    }
}

// MARK: - PHPickerViewControllerDelegate

extension CreateGroupChatViewController: PHPickerViewControllerDelegate {
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        picker.dismiss(animated: true)
        
        guard let result = results.first else { return }
        
        result.itemProvider.loadObject(ofClass: UIImage.self) { [weak self] object, error in
            if let image = object as? UIImage {
                DispatchQueue.main.async {
                    guard let self = self else { return }
                    self.headerView.groupNameView.configureImageView(image: image)
                    self.uploadImage(image)
                }
            }
        }
    }
}
