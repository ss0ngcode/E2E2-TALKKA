/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  experimental: {
    appDir: true,
  },
  api: {
    baseUrl: "https://talkka-bus.duckdns.org",
  },
}

export default nextConfig
